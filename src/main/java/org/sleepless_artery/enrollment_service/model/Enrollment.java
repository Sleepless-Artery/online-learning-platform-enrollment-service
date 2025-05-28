package org.sleepless_artery.enrollment_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sleepless_artery.enrollment_service.model.id.EnrollmentId;

import java.io.Serial;
import java.io.Serializable;


@Entity
@Table(name = "enrollments")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private EnrollmentId enrollmentId;
}
