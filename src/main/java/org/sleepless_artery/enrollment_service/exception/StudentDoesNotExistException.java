package org.sleepless_artery.enrollment_service.exception;

public class StudentDoesNotExistException extends RuntimeException {
    public StudentDoesNotExistException(String message) {
        super(message);
    }
}
