package com.implemica.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel(description = "Used to return a jwt token upon successful admin authorization.")
public class AuthorizationResponse {
    @ApiModelProperty(value = "Email address admin who logged in.", name = "email", example = "admin")
    String username;
    @ApiModelProperty(value = "JWT token to confirm authorization on the endpoint.", name = "token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBhZG1pbi5jb20iLCJyb2xlIjoiQURNSU4iLCJpYXQiOjE2NzAzMjAwNDMsImV4cCI6MTY3MDMyMzY0M30.AriLxRSb1htNjTZLCeZ5eKx_O8RANtaVeL1ust7EUXY")
    String token;
}
