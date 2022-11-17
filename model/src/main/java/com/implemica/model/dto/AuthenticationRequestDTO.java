package com.implemica.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AuthenticationRequestDTO {
    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(min = 4, max=16, message = "password is length not from 4 to 16")
    private String password;
}
