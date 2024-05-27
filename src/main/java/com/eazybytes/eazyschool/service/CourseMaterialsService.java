package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.model.CourseMaterials;
import com.eazybytes.eazyschool.model.Courses;
import com.eazybytes.eazyschool.repository.CourseMaterialsRepository;
import com.eazybytes.eazyschool.repository.CoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CourseMaterialsService {

    @Autowired
    CoursesRepository coursesRepository;
    @Autowired
    private CourseMaterialsRepository courseMaterialsRepository;

    public Map<Integer, Integer> countAndMapCourseMaterialsToCourses(Set<Courses> coursesList) {

        Map<Integer, Integer> mapIdandSum = new HashMap<>();

        for (Courses course : coursesList) {
            mapIdandSum.put(course.getCourseId(), courseMaterialsRepository.findByCourse_courseId(course.getCourseId()).size());
        }

        return mapIdandSum;
    }

    public CourseMaterials saveCourseMaterial(Integer courseId, String filepath, CourseMaterials courseMaterials) throws Exception {
        Optional<Courses> course = coursesRepository.findById(courseId);
        if (course.isPresent()) {
            if (course.get().getCourseMaterials().size() < 2) courseMaterials.setCourse(course.get());
            else throw new Exception("Nuk mund te shtohen me shume se 8 materiale!");
        }
        courseMaterials.setFilePath(filepath);
        return courseMaterialsRepository.save(courseMaterials);
    }

    public List<CourseMaterials> retrieveCourseMaterialsOfCourse(Integer courseId) {
        return courseMaterialsRepository.findByCourse_courseId(courseId);
    }

    public Integer deleteCourseMaterial(Integer materialId) {
        Optional<CourseMaterials> materials = courseMaterialsRepository.findById(materialId);
        Integer courseId = null;
        if (materials.isPresent()) {
            courseId = materials.get().getCourse().getCourseId();
            courseMaterialsRepository.deleteById(materialId);
        }
        return courseId;
    }

    public Optional<CourseMaterials> findCourseMaterial(Integer courseId, Integer materialId) {
        return courseMaterialsRepository.findByCourseId_MaterialId(courseId, materialId);
    }
}
