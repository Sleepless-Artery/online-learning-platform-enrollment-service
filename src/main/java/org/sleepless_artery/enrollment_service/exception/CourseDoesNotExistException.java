package org.sleepless_artery.enrollment_service.exception;

public class CourseDoesNotExistException extends RuntimeException {
    public CourseDoesNotExistException(String message) {
        super(message);
    }
}
