package org.jboss.resteasy.core;

import org.jboss.resteasy.core.interception.jaxrs.JaxrsInterceptorRegistry;
import org.jboss.resteasy.core.interception.jaxrs.JaxrsInterceptorRegistryListener;
import org.jboss.resteasy.core.interception.jaxrs.PostMatchContainerRequestContext;
import org.jboss.resteasy.core.registry.SegmentNode;
import org.jboss.resteasy.resteasy_jaxrs.i18n.LogMessages;
import org.jboss.resteasy.specimpl.BuiltResponse;
import org.jboss.resteasy.spi.ApplicationException;
import org.jboss.resteasy.spi.AsyncResponseProvider;
import org.jboss.resteasy.spi.AsyncStreamProvider;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.InjectorFactory;
import org.jboss.resteasy.spi.MethodInjector;
import org.jboss.resteasy.spi.ResourceFactory;
import org.jboss.resteasy.spi.ResteasyAsynchronousResponse;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.spi.ResteasyUriInfo;
import org.jboss.resteasy.spi.UnhandledException;
import org.jboss.resteasy.spi.metadata.ResourceMethod;
import org.jboss.resteasy.spi.validation.GeneralValidator;
import org.jboss.resteasy.spi.validation.GeneralValidatorCDI;
import org.jboss.resteasy.util.FeatureContextDelegate;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.WriterInterceptor;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class ResourceMethodInvoker implements ResourceInvoker, JaxrsInterceptorRegistryListener
{
   protected MethodInjector methodInjector;
   protected InjectorFactory injector;
   protected ResourceFactory resource;
   protected ResteasyProviderFactory parentProviderFactory;
   protected ResteasyProviderFactory resourceMethodProviderFactory;
   protected ResourceMethod method;
   protected Annotation[] methodAnnotations;
   protected ContainerRequestFilter[] requestFilters;
   protected ContainerResponseFilter[] responseFilters;
   protected WriterInterceptor[] writerInterceptors;
   protected ConcurrentHashMap<String, AtomicLong> stats = new ConcurrentHashMap<String, AtomicLong>();
   protected GeneralValidator validator;
   protected boolean isValidatable;
   protected boolean methodIsValidatable;
   protected ResourceInfo resourceInfo;

   protected boolean expectsBody;



   public ResourceMethodInvoker(ResourceMethod method, InjectorFactory injector, ResourceFactory resource, ResteasyProviderFactory providerFactory)
   {
      this.injector = injector;
      this.resource = resource;
      this.parentProviderFactory = providerFactory;
      this.method = method;
      this.methodAnnotations = this.method.getAnnotatedMethod().getAnnotations();

       resourceInfo = new ResourceInfo()
      {
         @Override
         public Method getResourceMethod()
         {
            return ResourceMethodInvoker.this.method.getAnnotatedMethod();
         }

         @Override
         public Class<?> getResourceClass()
         {
            return ResourceMethodInvoker.this.method.getResourceClass().getClazz();
         }
      };

      this.resourceMethodProviderFactory = new ResteasyProviderFactory(providerFactory);
      for (DynamicFeature feature : providerFactory.getServerDynamicFeatures())
      {
         feature.configure(resourceInfo, new FeatureContextDelegate(resourceMethodProviderFactory));
      }

      this.methodInjector = injector.createMethodInjector(method, resourceMethodProviderFactory);

      // hack for when message contentType == null
      // and @Consumes is on the class
      expectsBody = this.methodInjector.expectsBody();

      requestFilters = resourceMethodProviderFactory.getContainerRequestFilterRegistry().postMatch(method.getResourceClass().getClazz(), method.getAnnotatedMethod());
      responseFilters = resourceMethodProviderFactory.getContainerResponseFilterRegistry().postMatch(method.getResourceClass().getClazz(), method.getAnnotatedMethod());
      writerInterceptors = resourceMethodProviderFactory.getServerWriterInterceptorRegistry().postMatch(method.getResourceClass().getClazz(), method.getAnnotatedMethod());


      // we register with parent to lisen for redeploy evens
      providerFactory.getContainerRequestFilterRegistry().getListeners().add(this);
      providerFactory.getContainerResponseFilterRegistry().getListeners().add(this);
      providerFactory.getServerWriterInterceptorRegistry().getListeners().add(this);
      ContextResolver<GeneralValidator> resolver = providerFactory.getContextResolver(GeneralValidator.class, MediaType.WILDCARD_TYPE);
      if (resolver != null)
      {
         validator = providerFactory.getContextResolver(GeneralValidator.class, MediaType.WILDCARD_TYPE).getContext(null);
      }
      if (validator != null)
      {
         if (validator instanceof GeneralValidatorCDI)
         {
            isValidatable = GeneralValidatorCDI.class.cast(validator).isValidatable(getMethod().getDeclaringClass(), injector);
         }
         else
         {
            isValidatable = validator.isValidatable(getMethod().getDeclaringClass());
         }
         methodIsValidatable = validator.isMethodValidatable(getMethod());
      }
   }

   public void cleanup()
   {
      parentProviderFactory.getContainerRequestFilterRegistry().getListeners().remove(this);
      parentProviderFactory.getContainerResponseFilterRegistry().getListeners().remove(this);
      parentProviderFactory.getServerWriterInterceptorRegistry().getListeners().remove(this);
      for (ValueInjector param : methodInjector.getParams())
      {
         if (param instanceof MessageBodyParameterInjector)
         {
            parentProviderFactory.getServerReaderInterceptorRegistry().getListeners().remove(param);
         }
      }
   }

   public void registryUpdated(JaxrsInterceptorRegistry registry)
   {
      this.resourceMethodProviderFactory = new ResteasyProviderFactory(parentProviderFactory);
      for (DynamicFeature feature : parentProviderFactory.getServerDynamicFeatures())
      {
         feature.configure(resourceInfo, new FeatureContextDelegate(resourceMethodProviderFactory));
      }
      if (registry.getIntf().equals(WriterInterceptor.class))
      {
         writerInterceptors = resourceMethodProviderFactory.getServerWriterInterceptorRegistry().postMatch(method.getResourceClass().getClazz(), method.getAnnotatedMethod());
      }
      else if (registry.getIntf().equals(ContainerRequestFilter.class))
      {
         requestFilters = resourceMethodProviderFactory.getContainerRequestFilterRegistry().postMatch(method.getResourceClass().getClazz(), method.getAnnotatedMethod());
      }
      else if (registry.getIntf().equals(ContainerResponseFilter.class))
      {
         responseFilters = resourceMethodProviderFactory.getContainerResponseFilterRegistry().postMatch(method.getResourceClass().getClazz(), method.getAnnotatedMethod());
      }
   }


   protected void incrementMethodCount(String httpMethod)
   {
      AtomicLong stat = stats.get(httpMethod);
      if (stat == null)
      {
         stat = new AtomicLong();
         AtomicLong old = stats.putIfAbsent(httpMethod, stat);
         if (old != null) stat = old;
      }
      stat.incrementAndGet();
   }

   /**
    * Key is httpMethod called
    *
    * @return
    */
   public Map<String, AtomicLong> getStats()
   {
      return stats;
   }



   public ContainerRequestFilter[] getRequestFilters()
   {
      return requestFilters;
   }

   public ContainerResponseFilter[] getResponseFilters()
   {
      return responseFilters;
   }

   public WriterInterceptor[] getWriterInterceptors()
   {
      return writerInterceptors;
   }

   public Type getGenericReturnType()
   {
      return method.getGenericReturnType();
   }

   public Class<?> getResourceClass()
   {
      return method.getResourceClass().getClazz();
   }

   public Annotation[] getMethodAnnotations()
   {
      return methodAnnotations;
   }



   @Override
   public Method getMethod()
   {
      return method.getMethod();
   }

   public Object invokeDryRun(HttpRequest request, HttpResponse response) {
      Object target = resource.createResource(request, response, resourceMethodProviderFactory);
      return invokeDryRun(request, response, target);
   }


   public BuiltResponse invoke(HttpRequest request, HttpResponse response)
   {
      Object target = resource.createResource(request, response, resourceMethodProviderFactory);
      return invoke(request, response, target);
   }

   public Object invokeDryRun(HttpRequest request, HttpResponse response, Object target)
   {
      request.setAttribute(ResourceMethodInvoker.class.getName(), this);
      incrementMethodCount(request.getHttpMethod());
      ResteasyUriInfo uriInfo = (ResteasyUriInfo) request.getUri();
      if (method.getPath() != null)
      {
         uriInfo.pushMatchedURI(uriInfo.getMatchingPath());
      }
      uriInfo.pushCurrentResource(target);
      Object rtn = invokeOnTargetDryRun(request, response, target);
      return rtn;
   }
   
   public BuiltResponse invoke(HttpRequest request, HttpResponse response, Object target)
   {
      request.setAttribute(ResourceMethodInvoker.class.getName(), this);
      incrementMethodCount(request.getHttpMethod());
      ResteasyUriInfo uriInfo = (ResteasyUriInfo) request.getUri();
      if (method.getPath() != null)
      {
         uriInfo.pushMatchedURI(uriInfo.getMatchingPath());
      }
      uriInfo.pushCurrentResource(target);
      BuiltResponse rtn = invokeOnTarget(request, response, target);
      return rtn;
   }
   
   protected Object invokeOnTargetDryRun(HttpRequest request, HttpResponse response, Object target)
   {
      ResteasyProviderFactory.pushContext(ResourceInfo.class, resourceInfo);  // we don't pop so writer interceptors can get at this

      Object rtn = null;
      try
      {
         rtn = methodInjector.invoke(request, response, target);
      }
      catch (RuntimeException ex)
      {
        throw new ProcessingException(ex);

      }
      return rtn;
   }

   protected BuiltResponse invokeOnTarget(HttpRequest request, HttpResponse response, Object target)
   {
      ResteasyProviderFactory.pushContext(ResourceInfo.class, resourceInfo);  // we don't pop so writer interceptors can get at this

      PostMatchContainerRequestContext requestContext = new PostMatchContainerRequestContext(request, this, requestFilters, 
            () -> invokeOnTargetAfterFilter(request, response, target));
      // let it handle the continuation
      return requestContext.filter();
   }   

   protected BuiltResponse invokeOnTargetAfterFilter(HttpRequest request, HttpResponse response, Object target)
   {
      if (validator != null)
      {
         if (isValidatable)
         {
            validator.validate(request, target);
         }
         if (methodIsValidatable)
         {
            request.setAttribute(GeneralValidator.class.getName(), validator);
         }
         else if (isValidatable)
         {
            validator.checkViolations(request);
         }
      }
      
      AsyncResponseConsumer asyncStreamResponseConsumer = null;
      @SuppressWarnings("rawtypes")
      AsyncResponseProvider asyncResponseProvider = resourceMethodProviderFactory.getAsyncResponseProvider(method.getReturnType());
      @SuppressWarnings("rawtypes")
      AsyncStreamProvider asyncStreamProvider = null;
      
      if (asyncResponseProvider != null)
      {
         asyncStreamResponseConsumer = AsyncResponseConsumer.makeAsyncResponseConsumer(this, asyncResponseProvider);
      }
      else
      {
         asyncStreamProvider = resourceMethodProviderFactory.getAsyncStreamProvider(method.getReturnType());
         if (asyncStreamProvider != null)
         {
            asyncStreamResponseConsumer = AsyncResponseConsumer.makeAsyncResponseConsumer(this, asyncStreamProvider);
         }
      }
      

      Object rtn = null;
      try
      {
         rtn = methodInjector.invoke(request, response, target);
      }
      catch (RuntimeException ex)
      {
         if (asyncStreamResponseConsumer != null)
         {
            // WARNING: this can throw if the exception is not mapped by the user, in
            // which case we haven't completed the connection and called the callbacks
            try 
            {
               asyncStreamResponseConsumer.internalResume(ex);
            }
            catch(UnhandledException x) 
            {
               // make sure we call the callbacks before throwing to the container
               request.getAsyncContext().getAsyncResponse().completionCallbacks(ex);
               throw x;
            }
            asyncStreamResponseConsumer.complete(ex);
            return null;
         }
         else if (request.getAsyncContext().isSuspended())
         {
            try
            {
               request.getAsyncContext().getAsyncResponse().resume(ex);
            }
            catch (Exception e)
            {
               LogMessages.LOGGER.errorResumingFailedAsynchOperation(e);
            }
            return null;
         }
         else
         {
            throw ex;
         }

      }

      if(asyncStreamResponseConsumer != null)
      {
         asyncStreamResponseConsumer.subscribe(rtn);
         return null;
      }
      if (request.getAsyncContext().isSuspended())
      {
         if(method.isAsynchronous())
            return null;
         // resume a sync request that got turned async by filters
         request.getAsyncContext().getAsyncResponse().resume(rtn);
         return null;
      }
      if (request.wasForwarded())
      {
         return null;
      }
      if (rtn == null || method.getReturnType().equals(void.class))
      {
         BuiltResponse build = (BuiltResponse) Response.noContent().build();
         build.addMethodAnnotations(getMethodAnnotations());
         return build;
      }
      if (Response.class.isAssignableFrom(method.getReturnType()) || rtn instanceof Response)
      {
         if (!(rtn instanceof BuiltResponse))
         {
            Response r = (Response)rtn;
            Headers<Object> metadata = new Headers<Object>();
            metadata.putAll(r.getMetadata());
            rtn = new BuiltResponse(r.getStatus(), r.getStatusInfo().getReasonPhrase(), metadata, r.getEntity(), null);
         }
         BuiltResponse rtn1 = (BuiltResponse) rtn;
         rtn1.addMethodAnnotations(getMethodAnnotations());
         if (rtn1.getGenericType() == null)
         {
            if (getMethod().getReturnType().equals(Response.class))
            {
               rtn1.setGenericType(rtn1.getEntityClass());
            }
            else
            {
               rtn1.setGenericType(method.getGenericReturnType());
            }
         }
         return rtn1;
      }

      Response.ResponseBuilder builder = Response.ok(rtn);
      BuiltResponse jaxrsResponse = (BuiltResponse)builder.build();
      if (jaxrsResponse.getGenericType() == null)
      {
         if (getMethod().getReturnType().equals(Response.class))
         {
            jaxrsResponse.setGenericType(jaxrsResponse.getEntityClass());
         }
         else
         {
            jaxrsResponse.setGenericType(method.getGenericReturnType());
         }
      }
      jaxrsResponse.addMethodAnnotations(getMethodAnnotations());
      return jaxrsResponse;
   }

   public void initializeAsync(ResteasyAsynchronousResponse asyncResponse)
   {
      asyncResponse.setAnnotations(method.getAnnotatedMethod().getAnnotations());
      asyncResponse.setWriterInterceptors(writerInterceptors);
      asyncResponse.setResponseFilters(responseFilters);
      asyncResponse.setMethod(this);
   }

   public boolean doesProduce(List<? extends MediaType> accepts)
   {
      if (accepts == null || accepts.size() == 0)
      {
         //System.out.println("**** no accepts " +" method: " + method);
         return true;
      }
      if (method.getProduces().length == 0)
      {
         //System.out.println("**** no produces " +" method: " + method);
         return true;
      }

      for (MediaType accept : accepts)
      {
         for (MediaType type : method.getProduces())
         {
            if (type.isCompatible(accept))
            {
               return true;
            }
         }
      }
      return false;
   }

   public boolean doesConsume(MediaType contentType)
   {
      boolean matches = false;
      if (method.getConsumes().length == 0 || (contentType == null && !expectsBody)) return true;

      if (contentType == null)
      {
         contentType = MediaType.APPLICATION_OCTET_STREAM_TYPE;
      }
      for (MediaType type : method.getConsumes())
      {
         if (type.isCompatible(contentType))
         {
            matches = true;
            break;
         }
      }
      return matches;
   }

   public MediaType resolveContentType(HttpRequest in, Object entity)
   {
      MediaType chosen = (MediaType)in.getAttribute(SegmentNode.RESTEASY_CHOSEN_ACCEPT);
      if (chosen != null  && !chosen.equals(MediaType.WILDCARD_TYPE))
      {
         return chosen;
      }

      List<MediaType> accepts = in.getHttpHeaders().getAcceptableMediaTypes();

      if (accepts == null || accepts.size() == 0)
      {
         if (method.getProduces().length == 0) return MediaType.WILDCARD_TYPE;
         else return method.getProduces()[0];
      }

      if (method.getProduces().length == 0)
      {
         return resolveContentTypeByAccept(accepts, entity);
      }

      for (MediaType accept : accepts)
      {
         for (MediaType type : method.getProduces())
         {
            if (type.isCompatible(accept)) return type;
         }
      }
      return MediaType.WILDCARD_TYPE;
   }

   @SuppressWarnings(value = "unchecked")
   protected MediaType resolveContentTypeByAccept(List<MediaType> accepts, Object entity)
   {
      if (accepts == null || accepts.size() == 0 || entity == null)
      {
         return MediaType.WILDCARD_TYPE;
      }
      Class clazz = entity.getClass();
      Type type = this.method.getGenericReturnType();
      if (entity instanceof GenericEntity)
      {
         GenericEntity gen = (GenericEntity) entity;
         clazz = gen.getRawType();
         type = gen.getType();
      }
      for (MediaType accept : accepts)
      {
         if (resourceMethodProviderFactory.getMessageBodyWriter(clazz, type, method.getAnnotatedMethod().getAnnotations(), accept) != null)
         {
            return accept;
         }
      }
      return MediaType.WILDCARD_TYPE;
   }
   
   
   

   public Set<String> getHttpMethods()
   {
      return method.getHttpMethods();
   }

   public MediaType[] getProduces()
   {
      return method.getProduces();
   }

   public MediaType[] getConsumes()
   {
      return method.getConsumes();
   }

   public void markMethodAsAsync()
   {
      method.markAsynchronous();
   }
}
