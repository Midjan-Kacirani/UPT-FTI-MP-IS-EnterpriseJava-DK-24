package com.eazybytes.eazyschool.repository;

import com.eazybytes.eazyschool.model.CourseMaterials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseMaterialsRepository extends JpaRepository<CourseMaterials, Integer> {
    List<CourseMaterials> findByCourse_courseId(int courseId);
}
