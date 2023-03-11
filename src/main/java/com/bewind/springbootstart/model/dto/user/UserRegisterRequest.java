package com.bewind.springbootstart.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3195612716373120793L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
