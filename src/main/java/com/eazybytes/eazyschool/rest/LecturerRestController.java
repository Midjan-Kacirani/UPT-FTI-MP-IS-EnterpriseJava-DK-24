package com.eazybytes.eazyschool.rest;

import com.eazybytes.eazyschool.dto.PersonDTO;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/api/lecturer")
public class LecturerRestController {

    @Autowired
    PersonService service;

    @PostMapping("/save")
    public ResponseEntity<PersonDTO> saveLecturer(@RequestBody Person person){
        return new ResponseEntity<>(service.saveAndReturnDTO(person), HttpStatus.CREATED);
    }
}
