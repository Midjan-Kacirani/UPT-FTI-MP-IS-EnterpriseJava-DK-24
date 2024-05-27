package com.eazybytes.eazyschool.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class CourseRatingId implements Serializable {

    @Column(name = "course_id")
    private int courseId;

    @Column(name = "person_id")
    private int personId;


}
