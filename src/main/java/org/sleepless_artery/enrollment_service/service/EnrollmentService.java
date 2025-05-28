package org.sleepless_artery.enrollment_service.service;

import org.sleepless_artery.enrollment_service.model.Enrollment;

import java.util.List;


public interface EnrollmentService {

    List<Enrollment> getEnrollments();

    List<Enrollment> getEnrollmentsByStudentId(Long studentId);

    List<Enrollment> getEnrollmentsByCourseId(Long courseId);

    Enrollment createEnrollment(Long studentId, Long courseId);

    void deleteEnrollment(Long studentId, Long courseId);

    void deleteEnrollmentsByStudentId(Long studentId);

    void deleteEnrollmentsByCourseId(Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}
