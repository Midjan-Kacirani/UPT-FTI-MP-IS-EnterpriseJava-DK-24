package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.repository.CourseRatingRepository;
import com.eazybytes.eazyschool.repository.CoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eazybytes.eazyschool.model.CourseRating;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseRatingService {
  @Autowired
  CourseRatingRepository courseRatingRepository;

  @Autowired
  CoursesRepository coursesRepository;

  public double calculateCourseRating(int courseId) {
    List<CourseRating> ratings = courseRatingRepository.findByIdCourseId(courseId);

    int sum = 0;
    for (CourseRating rating : ratings) {
      sum += rating.getRating();
    }

    double averageRating = 0.0;
    if (!ratings.isEmpty()) {
      averageRating = (double) sum / ratings.size();
    }

    return averageRating;
  }

  public Map<Integer, Double> calculateAverageRatingsForCourses() {
    Map<Integer, Double> averageRatingsMap = new HashMap<>();

    // Retrieve all course IDs
    List<Integer> courseIds = coursesRepository.findAllCourseIds();

    // Calculate average rating for each course
    for (Integer courseId : courseIds) {
      double averageRating = calculateCourseRating(courseId);
      averageRatingsMap.put(courseId, averageRating);
    }

    return averageRatingsMap;
  }
}
