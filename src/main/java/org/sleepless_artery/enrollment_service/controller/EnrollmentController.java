package org.sleepless_artery.enrollment_service.controller;

import lombok.RequiredArgsConstructor;
import org.sleepless_artery.enrollment_service.model.Enrollment;
import org.sleepless_artery.enrollment_service.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;


    @GetMapping
    public ResponseEntity<Boolean> checkEnrollment(@RequestParam Long studentId, @RequestParam Long courseId) {
        return ResponseEntity.ok(enrollmentService.existsByStudentIdAndCourseId(studentId, courseId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getEnrollments());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getStudentEnrollments(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudentId(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getCourseEnrollments(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourseId(courseId));
    }

    @PostMapping
    public ResponseEntity<Enrollment> createEnrollment(@RequestParam Long studentId, @RequestParam Long courseId) {
        return ResponseEntity.ok(enrollmentService.createEnrollment(studentId, courseId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteEnrollment(@RequestParam Long studentId, @RequestParam Long courseId) {
        enrollmentService.deleteEnrollment(studentId, courseId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<Void> deleteStudentEnrollments(@PathVariable Long studentId) {
        enrollmentService.deleteEnrollmentsByStudentId(studentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/course/{courseId}")
    public ResponseEntity<Void> deleteCourseEnrollments(@PathVariable Long courseId) {
        enrollmentService.deleteEnrollmentsByCourseId(courseId);
        return ResponseEntity.noContent().build();
    }
}
