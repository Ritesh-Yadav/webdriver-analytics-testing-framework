package io.github.riteshyadav.dao;

import lombok.Data;

public @Data
class RegistrationDao {

    private String id;
    private String title;
    private String firstName;
    private String lastName;
    private String dobDay;
    private String dobMonth;
    private String dobYear;
    private String homePhone;
    private String mobilePhone;
    private String email;
    private String password;
    private String houseNumber;
    private String postcode;

}
