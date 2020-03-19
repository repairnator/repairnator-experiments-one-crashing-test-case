package io.vertx.up.exception;

import io.vertx.core.http.HttpStatusCode;

public class _401QiyAuthorizedException extends WebException {

    public _401QiyAuthorizedException(final Class<?> clazz,
                                      final String code,
                                      final String message) {
        super(clazz, code, message);
    }

    @Override
    public int getCode() {
        return -20001;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }
}
