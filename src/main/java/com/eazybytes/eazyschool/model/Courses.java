package com.eazybytes.eazyschool.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Courses extends BaseEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private int courseId;

    private String name;

    private String fees;

    private String Category;

    private String longDescription;

    private String shortDescription;

    private String coursePicture;

    private Double rating;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private Set<Person> persons = new HashSet<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CourseMaterials> courseMaterials = new HashSet<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CourseRating> ratings = new HashSet<>();

    @Transient
    public String getPhotosImagePath(){
        if(coursePicture == null || courseId <0)
            return null;
        return "assets/CourseImages/"+courseId+"/"+coursePicture;
    }

}
