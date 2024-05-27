package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.dto.PersonDTO;
import com.eazybytes.eazyschool.model.CourseRatingId;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.model.Roles;
import com.eazybytes.eazyschool.repository.CourseRatingRepository;
import com.eazybytes.eazyschool.repository.PersonRepository;
import com.eazybytes.eazyschool.repository.RolesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CourseRatingRepository courseRatingRepository;

    public boolean createNewPerson(Person person, String roleInput){
        boolean isSaved = false;
        Roles role = rolesRepository.getByRoleName(roleInput);
        person.setRoles(role);
        person.setPwd(passwordEncoder.encode(person.getPwd()));
        person = personRepository.save(person);
        if (null != person && person.getPersonId() > 0)
        {
            isSaved = true;
        }
        return isSaved;
    }

    public Set<Person> findByRoleName(String role){
        return personRepository.findByRoleName(role);
    }

    @Transactional
    public boolean checkIfCourseExistsForPerson(int personId, int courseId) {
        return personRepository.existsCourseForPerson(personId, courseId);
    }


    public boolean hasRatedCourse(int personId, int courseId) {
        CourseRatingId courseRatingId = new CourseRatingId();
        courseRatingId.setPersonId(personId);
        courseRatingId.setCourseId(courseId);

        return courseRatingRepository.findById(courseRatingId).isPresent();
    }

    public PersonDTO saveAndReturnDTO(Person person) {
        PersonDTO personDTO = null;
        if(createNewPerson(person, EazySchoolConstants.LECTURER_ROLE)){
            personDTO = new PersonDTO();
            personDTO.setName(personDTO.getName());
            personDTO.setEmail(personDTO.getEmail());
            personDTO.setMobileNumber(personDTO.getMobileNumber());
            personDTO.setRoles(rolesRepository.getByRoleName(EazySchoolConstants.LECTURER_ROLE));
        }
        return personDTO;
    }
}
