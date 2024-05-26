package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.model.Courses;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.model.Roles;
import com.eazybytes.eazyschool.repository.CoursesRepository;
import com.eazybytes.eazyschool.repository.PersonRepository;
import com.eazybytes.eazyschool.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CourseService {
    @Autowired
    CoursesRepository coursesRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    RolesRepository rolesRepository;

    public Courses findById(Integer id){
        return coursesRepository.findById(id).orElse(null);
    }

    public Set<Person> findByRole(int course_id, String role){
        Roles byRoleName = rolesRepository.getByRoleName(role);
        Set<Person> personSet = null;
        if(byRoleName != null) personSet = personRepository.findPersonsByCourseIdAndRoleId(course_id, byRoleName.getRoleId());
        return personSet;
    }
}
