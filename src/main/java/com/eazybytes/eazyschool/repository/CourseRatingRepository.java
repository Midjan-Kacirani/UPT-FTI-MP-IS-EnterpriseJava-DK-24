package com.eazybytes.eazyschool.repository;

import com.eazybytes.eazyschool.model.CourseRating;
import com.eazybytes.eazyschool.model.CourseRatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRatingRepository extends JpaRepository<CourseRating, CourseRatingId> {
  List<CourseRating> findByIdCourseId(int courseId);
}