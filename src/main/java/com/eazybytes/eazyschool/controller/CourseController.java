package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.config.EazySchoolProps;
import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.model.Courses;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.ContactRepository;
import com.eazybytes.eazyschool.repository.CoursesRepository;
import com.eazybytes.eazyschool.service.PersonService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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


    @GetMapping("/courses")
    public ModelAndView getCourses(Model model) {
        List<Courses> courses = courseService.findAll();

        ModelAndView modelAndView = new ModelAndView("courses");
        modelAndView.addObject("courses", courses);

        return modelAndView;
    }


    @GetMapping("/courses/check")
    public ModelAndView checkProfile(@RequestParam("courseId") int courseId, HttpSession session, Model model){

        Person person = (Person) session.getAttribute("loggedInPerson");


        ModelAndView modelAndView = new ModelAndView("courseDescription.html");

        Optional<Courses> optionalCourses = courseService.findById(courseId);
        Courses courses = optionalCourses.orElse(null);



        boolean exists = personService.checkIfCourseExistsForPerson(person.getPersonId(), courseId);

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
        } else {
            // Set a default view name if the course doesn't exist for the person
            modelAndView.setViewName("");
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


}

