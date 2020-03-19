package io.vertx.up.exception;

import io.vertx.core.http.HttpStatusCode;

public class _501SharedDataModeException extends WebException {

    public _501SharedDataModeException(final Class<?> clazz,
                                       final boolean async) {
        super(clazz, async, !async);
    }

    @Override
    public int getCode() {
        return -60034;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
