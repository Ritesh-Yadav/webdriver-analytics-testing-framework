package io.github.riteshyadav.dao;

import lombok.Data;

public @Data
class LoginDao {
    private String id;
    private String postcode;
    private String username;
    private String password;

}
