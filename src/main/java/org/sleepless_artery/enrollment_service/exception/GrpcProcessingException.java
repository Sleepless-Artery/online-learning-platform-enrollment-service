package org.sleepless_artery.enrollment_service.exception;

public class GrpcProcessingException extends RuntimeException {
    public GrpcProcessingException(String message) {
        super(message);
    }
}
