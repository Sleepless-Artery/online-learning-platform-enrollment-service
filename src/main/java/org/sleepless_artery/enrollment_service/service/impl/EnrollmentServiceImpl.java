package org.sleepless_artery.enrollment_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sleepless_artery.enrollment_service.exception.CourseDoesNotExistException;
import org.sleepless_artery.enrollment_service.exception.EnrollmentAlreadyExistsException;
import org.sleepless_artery.enrollment_service.exception.EnrollmentNotFoundException;
import org.sleepless_artery.enrollment_service.exception.StudentDoesNotExistException;
import org.sleepless_artery.enrollment_service.grpc.client.CourseVerificationServiceGrpcClient;
import org.sleepless_artery.enrollment_service.grpc.client.UserVerificationServiceGrpcClient;
import org.sleepless_artery.enrollment_service.model.Enrollment;
import org.sleepless_artery.enrollment_service.model.id.EnrollmentId;
import org.sleepless_artery.enrollment_service.repository.EnrollmentRepository;
import org.sleepless_artery.enrollment_service.service.EnrollmentService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    private final CourseVerificationServiceGrpcClient courseVerificationServiceGrpcClient;
    private final UserVerificationServiceGrpcClient userVerificationServiceGrpcClient;


    @Override
    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollments() {
        return enrollmentRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "enrollments", key = "'student:' + #studentId")
    public List<Enrollment> getEnrollmentsByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "enrollments", key = "'course:' + #courseId")
    public List<Enrollment> getEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "enrollments", key = "'pair:' + #studentId.toString() + ':' + #courseId.toString()")
    public boolean existsByStudentIdAndCourseId(Long studentId, Long courseId) {
        return enrollmentRepository.existsById(new EnrollmentId(studentId, courseId));
    }


    @Override
    @Transactional
    @CacheEvict(value = "enrollments", allEntries = true)
    public Enrollment createEnrollment(Long studentId, Long courseId) {

        if (studentId == null || !userVerificationServiceGrpcClient.verifyUserExistence(studentId)) {
            log.warn("Student with ID '{}' doesn't exist", studentId);
            throw new StudentDoesNotExistException("Student with ID " + studentId + " doesn't exist");
        }

        if (courseId == null || !courseVerificationServiceGrpcClient.verifyCourseExistence(courseId)) {
            log.warn("Course with ID '{}' doesn't exist", courseId);
            throw new CourseDoesNotExistException("Course with ID " + courseId + " doesn't exist");
        }

        var enrollmentId = new EnrollmentId(studentId, courseId);

        if (enrollmentRepository.existsById(enrollmentId)) {
            log.warn("Enrollment already exists");
            throw new EnrollmentAlreadyExistsException("User already enrolled to the course");
        }

        return enrollmentRepository.save(new Enrollment(enrollmentId));
    }


    @Override
    @Transactional
    @CacheEvict(value = "enrollments", allEntries = true)
    public void deleteEnrollment(Long studentId, Long courseId) {
        var enrollmentId = new EnrollmentId(studentId, courseId);

        if (!enrollmentRepository.existsById(enrollmentId)) {
            log.warn("Enrollment does not exist");
            throw new EnrollmentNotFoundException("User is not enrolled to the course");
        }
        enrollmentRepository.deleteById(enrollmentId);
    }


    @Override
    @Transactional
    @CacheEvict(value = "enrollments", allEntries = true)
    public void deleteEnrollmentsByStudentId(Long studentId) {
        enrollmentRepository.deleteAllByStudentId(studentId);
    }


    @Override
    @Transactional
    @CacheEvict(value = "enrollments", allEntries = true)
    public void deleteEnrollmentsByCourseId(Long courseId) {
        enrollmentRepository.deleteAllByCourseId(courseId);
    }
}
