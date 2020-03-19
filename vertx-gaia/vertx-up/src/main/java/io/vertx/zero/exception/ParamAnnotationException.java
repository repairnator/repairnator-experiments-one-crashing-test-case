package io.vertx.zero.exception;

public class ParamAnnotationException extends UpException {

    public ParamAnnotationException(final Class<?> clazz,
                                    final String field,
                                    final int occurs) {
        super(clazz, field, occurs);
    }

    @Override
    public int getCode() {
        return -40030;
    }
}
