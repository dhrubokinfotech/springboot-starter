package com.disl.starter.exceptions;

public class NotFoundException extends RuntimeException {
    private Class<?> className;

    public NotFoundException(Class<?> className) {
        this.className = className;
    }

    public Class<?> getClassName() {
        return className;
    }

    public void setClassName(Class<?> className) {
        this.className = className;
    }
}
