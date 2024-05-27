package com.eazybytes.eazyschool.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class CourseRatingId implements Serializable {

    private int courseId;
    private int personId;


}
