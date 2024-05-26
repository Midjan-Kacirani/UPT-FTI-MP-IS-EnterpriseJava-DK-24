package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.Utilities.FileUploadUtil;
import com.eazybytes.eazyschool.model.CourseMaterials;
import com.eazybytes.eazyschool.model.Courses;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.CoursesRepository;
import com.eazybytes.eazyschool.repository.PersonRepository;
import com.eazybytes.eazyschool.service.CourseMaterialsService;
import com.eazybytes.eazyschool.service.CourseService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("lecturer")
public class LecturerController {

    @Autowired
    CourseMaterialsService courseMaterialsService;

    @Autowired
    CourseService courseService;


    @RequestMapping("/displayCourses")
    public ModelAndView showCourses(HttpSession session) {
        //Do te lexohen te gjithe kurset e pedegogeve
        //id e pedagogut do te merret nga session
        Person person = (Person) session.getAttribute("loggedInPerson");

        //Marrim nje set me course te profesorit
        Set<Courses> courses = person.getCourses();

        //Bejme nje mapping midis numrit te materialeve te hedhuar per course deri tani
        Map<Integer, Integer> mapIdAndSumOfNoMaterials = courseMaterialsService.countAndMapCourseMaterialsToCourses(courses);

        //I vendosim tek objektet e template html
        ModelAndView modelAndView = new ModelAndView("course_lecturer.html");
        modelAndView.addObject("username", person.getName());
        modelAndView.addObject("courses", courses);
        modelAndView.addObject("mapIdAndSumOfNoMaterials", mapIdAndSumOfNoMaterials);

        return modelAndView;
    }

    @GetMapping("/viewCourseMaterials")
    public ModelAndView viewCourseMaterials(@RequestParam Integer courseId) {
        ModelAndView modelAndView = new ModelAndView("course_materials.html");
        modelAndView.addObject("courses", courseService.findById(courseId));
        modelAndView.addObject("classMaterial", new CourseMaterials());
        modelAndView.addObject("courseMaterials", courseMaterialsService.retrieveCourseMaterialsOfCourse(courseId));
        return modelAndView;
    }

    @PostMapping("/addNewClassMaterial")
    public ModelAndView addNewClassMaterial(@RequestParam("courseId") Integer courseId, @ModelAttribute("classMaterial") CourseMaterials courseMaterial, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        CourseMaterials materialSaved = null;
        ModelAndView modelAndView = new ModelAndView("redirect:/lecturer/viewCourseMaterials?courseId=" + courseId);
        Exception exception = null;
        try {
            if ((file.getSize() / (1024 * 1024) > 2) || (!Objects.equals(file.getContentType(), "application/pdf")))
                throw new Exception("File duhet te jete PDF dhe me pak se 10 MB!");
            materialSaved = courseMaterialsService.saveCourseMaterial(courseId, fileName, courseMaterial);
            String uploadDir = "src/main/resources/static/assets/CourseMaterials/" + materialSaved.getCourse().getCourseId();
            try {
                FileUploadUtil.saveFile(uploadDir, fileName, file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            exception = e;

        }
        if (exception == null) {
            redirectAttributes.addFlashAttribute("successMessage", "Material added successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return modelAndView;
    }

    @PostMapping("/deleteCourseMaterial")
    public ModelAndView deleteCourseMaterial(@RequestParam("material_id") Integer courseMaterialId) {
        Integer courseId = courseMaterialsService.deleteCourseMaterial(courseMaterialId);
        return new ModelAndView("redirect:/lecturer/viewCourseMaterials?courseId=" + courseId);
    }

    @GetMapping("/viewStudentsEnrolled")
    public ModelAndView viewStudentsEnrolled(@RequestParam("courseId") Integer courseId){
        Set<Person> studentSet = courseService.findByRole(courseId, "STUDENT");
        ModelAndView modelAndView = new ModelAndView("course_students_enrolled.html");
        modelAndView.addObject("studentSet", studentSet);
        return modelAndView;
    }

}
