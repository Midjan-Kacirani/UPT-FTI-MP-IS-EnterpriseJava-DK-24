package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.*;
import com.eazybytes.eazyschool.repository.ContactRepository;
import com.eazybytes.eazyschool.repository.CourseRatingRepository;
import com.eazybytes.eazyschool.repository.CoursesRepository;
import com.eazybytes.eazyschool.service.CourseMaterialsService;
import com.eazybytes.eazyschool.service.CourseRatingService;
import com.eazybytes.eazyschool.service.PersonService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


@Controller
public class CourseController {

    @Autowired
    private CoursesRepository courseService;

    @Autowired
    private PersonService personService;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private CourseMaterialsService courseMaterialsService;
    @Autowired
    private CourseRatingRepository courseRatingRepository;

    @Autowired
    private CourseRatingService courseRatingService;


    @GetMapping("/courses")
    public ModelAndView getCourses(Model model) {
        List<Courses> courses = courseService.findAll();

        ModelAndView modelAndView = new ModelAndView("courses");
        modelAndView.addObject("courses", courses);
        modelAndView.addObject("courseRating", courseRatingService.calculateAverageRatingsForCourses());

        return modelAndView;
    }


    @GetMapping("/courses/check")
    public ModelAndView checkProfile(@RequestParam("courseId") int courseId, HttpSession session, Model model){

        Person person = (Person) session.getAttribute("loggedInPerson");


        ModelAndView modelAndView = new ModelAndView("courseDescription.html");

        Optional<Courses> optionalCourses = courseService.findById(courseId);
        Courses courses = optionalCourses.orElse(null);
        List<CourseMaterials> courseMaterials = courseMaterialsService.retrieveCourseMaterialsOfCourse(courseId);
        modelAndView.addObject("courseMaterials", courseMaterials);


        boolean exists = personService.checkIfCourseExistsForPerson(person.getPersonId(), courseId);
        boolean hasRated = personService.hasRatedCourse(person.getPersonId(), courseId);

        modelAndView.addObject("person", person);
        modelAndView.addObject("course", courses);
        modelAndView.addObject("status",  EazySchoolConstants.OPEN);

        //Shikoj nqs e ka bere me perpara ose jo nje request.
        //Nqs po, atehere butoni do te jete disabled
        //Nqs jo, butoni do te jete enabled
        if (exists) {
            modelAndView.setViewName("courseDescription.html");
            Optional<Contact> deletion = contactRepository.findSpecificStudentMessageSpecificSubject("DELETION", person.getEmail());
            if(deletion.isPresent()) modelAndView.addObject("disabled", true);
            else modelAndView.addObject("disabled", false);
            modelAndView.addObject("hasRated", hasRated);
            CourseRatingId courseRatingId = new CourseRatingId();
            courseRatingId.setCourseId(courseId);
            courseRatingId.setPersonId(person.getPersonId());
            modelAndView.addObject("courseRating", courseRatingRepository.findById(courseRatingId).orElse(new CourseRating()));

        } else {
            // Set a default view name if the course doesn't exist for the person
            modelAndView.setViewName("courseDescription_enrollrequest.html");
            Optional<Contact> enrollment = contactRepository.findSpecificStudentMessageSpecificSubject("ENROLLMENT", person.getEmail());
            if(enrollment.isPresent()) modelAndView.addObject("disabled", true);
            else modelAndView.addObject("disabled", false);
        }


        return modelAndView;
}

    @GetMapping("/courses/unregister")
    public String unRegister( @RequestParam("courseId") int courseId, HttpSession session, Model model){

        Person person = (Person) session.getAttribute("loggedInPerson");

        Contact contact = new Contact();

        contact.setSubject("DELETION");
        contact.setMessage("CourseID: "+courseId);

        contact.setEmail(person.getEmail());

        contactRepository.save(contact);


        return "redirect:../dashboard";
    }

    @GetMapping("/courses/register")
    public String register( @RequestParam("courseId") int courseId, HttpSession session, Model model){

        Person person = (Person) session.getAttribute("loggedInPerson");

        Contact contact = new Contact();

        contact.setSubject("ENROLLMENT");
        contact.setMessage("CourseID: "+courseId);

        contact.setEmail(person.getEmail());

        contactRepository.save(contact);


        return "redirect:../dashboard";
    }

    @GetMapping("/courses/downloadClassMaterial")
    public ResponseEntity<Resource> downloadClassMaterial(@RequestParam("courseId") Integer courseId, @RequestParam("materialId") Integer materialId) {
        // Logic to fetch file from server
        CourseMaterials material = courseMaterialsService.findCourseMaterial(courseId, materialId).get();
        Path filePath = Paths.get("src/main/resources/static/assets/CourseMaterials/" + courseId + "/" + material.getFilePath());
        Resource resource = new FileSystemResource(filePath.toFile());

        // Check if the file exists
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Set content type and disposition for the response
        String contentType = "application/pdf"; // Change this based on the file type
        String disposition = "attachment; filename=" + resource.getFilename();

        // Return ResponseEntity with file content
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                .body(resource);
    }

    @GetMapping("/courses/setRating")
    public String setRating(@RequestParam("courseId") int courseId, @RequestParam("rating") int rating, HttpSession session) {
        Person person = (Person) session.getAttribute("loggedInPerson");


        Optional<Courses> optionalCourse = courseService.findById(courseId);
        Courses course = optionalCourse.orElse(null);


        if (course != null && person != null) {
            boolean hasRated = personService.hasRatedCourse(person.getPersonId(), courseId);
            if (!hasRated) {
                CourseRating courseRating = new CourseRating();
                courseRating.setCourse(course);
                courseRating.setPerson(person);
                courseRating.setRating(rating);

                courseRatingRepository.save(courseRating);
            }
        }
        return "redirect:/courses/check?courseId=" + courseId;
    }


}

