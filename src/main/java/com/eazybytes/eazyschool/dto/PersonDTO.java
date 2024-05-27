package com.eazybytes.eazyschool.dto;

import com.eazybytes.eazyschool.model.Roles;
import lombok.Data;

@Data
public class PersonDTO {
    private String name;
    private String mobileNumber;
    private String email;
    private Roles roles;

}

