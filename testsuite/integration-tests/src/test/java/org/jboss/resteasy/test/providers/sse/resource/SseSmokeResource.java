package org.jboss.resteasy.test.providers.sse.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

@Path("/sse")
public class SseSmokeResource {

    @GET
    @Path("/events")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void sentEvents(@Context SseEventSink sseEventSink, @Context Sse sse) {

        try (SseEventSink sink = sseEventSink) {
         sseEventSink.send(sse.newEventBuilder()
                 .name("customObj")
                 .data(new SseSmokeUser("Zeytin", "zeytin@resteasy.org")).build());
        }
    }
}
