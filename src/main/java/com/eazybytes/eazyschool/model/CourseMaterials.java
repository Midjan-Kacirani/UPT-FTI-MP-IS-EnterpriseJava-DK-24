package com.eazybytes.eazyschool.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "course_materials")
@Data
public class CourseMaterials extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    private int id;


    private String name;

    private String filePath;

    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Courses course;

}
