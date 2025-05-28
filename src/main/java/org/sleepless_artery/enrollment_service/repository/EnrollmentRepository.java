package org.sleepless_artery.enrollment_service.repository;

import org.sleepless_artery.enrollment_service.model.Enrollment;
import org.sleepless_artery.enrollment_service.model.id.EnrollmentId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface EnrollmentRepository extends CrudRepository<Enrollment, EnrollmentId> {

    List<Enrollment> findAll();

    @Query("SELECT e from Enrollment e where e.enrollmentId.studentId = :studentId")
    List<Enrollment> findByStudentId(Long studentId);

    @Query("SELECT e from Enrollment e where e.enrollmentId.courseId = :courseId")
    List<Enrollment> findByCourseId(Long courseId);

    @Modifying
    @Query("delete from Enrollment e where e.enrollmentId.studentId = :studentId")
    void deleteAllByStudentId(Long studentId);

    @Modifying
    @Query("delete from Enrollment e where e.enrollmentId.courseId = :courseId")
    void deleteAllByCourseId(Long courseId);
}
