package com.implemica.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "Used to pass admin authorization data.")
public class AuthenticationRequestDTO {
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9])*$")
    @Size(min = 4, max = 20)
    @ApiModelProperty(value = "Admin email address.",name = "username",required = true,example = "admin")
    private String username;

    @NotNull
    @Size(min = 4, max=20, message = "password is length not from 4 to 20")
    @ApiModelProperty(value = "Admin password.",name = "password",required = true,example = "password")
    private String password;
}
