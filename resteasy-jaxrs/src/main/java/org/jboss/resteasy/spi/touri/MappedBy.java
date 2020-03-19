package org.jboss.resteasy.spi.touri;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * This annotation can be added to any object to perform a Resource/method based
 * lookup to create a URI template. From there, perform Java beans-based
 * transformation between URI template and bean properties. For example, take
 * the following bean:
 * </p>
 * <p/>
 * <pre>
 * MappedBy(Resource=FooResource.class, method="getFoo")
 * public class Foo
 * {
 * 	private int id;
 * 	// getters and setters
 * }
 *
 * @author <a href="mailto:sduskis@gmail.com">Solomon Duskis</a>
 * @version $Revision: 1 $
 * @Path("/foo") public class FooResource {
 * @GET
 * @Path("{id}")
 * @Produces(...) public Foo getFoo(@PathParam("id") Integer id){
 * ...
 * }
 * }
 * </pre>
 * <p/>
 * <p>
 * for a Foo f with id = 123, ObjectToURI.getInstance(f) = "/foo/123"
 * </p>
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MappedBy
{
   Class<?> resource();

   String method() default "";
}
