package com.eazybytes.eazyschool.repository;

import com.eazybytes.eazyschool.model.CourseMaterials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseMaterialsRepository extends JpaRepository<CourseMaterials, Integer> {
    List<CourseMaterials> findByCourse_courseId(int courseId);

    @Query(value = "SELECT * FROM Course_Materials WHERE course_Id = :courseId AND id = :id", nativeQuery = true)
    Optional<CourseMaterials> findByCourseId_MaterialId(@Param("courseId") Integer courseId, @Param("id")Integer materialId);
}
