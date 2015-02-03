package org.jcoffee.orm.exception;

/**
 * Created by Aleksandr Simonchuk on 31.01.15.
 */
public class AnnotationNotFoundException extends RuntimeException {
    public AnnotationNotFoundException(String message) {
        super(message);
    }
}
