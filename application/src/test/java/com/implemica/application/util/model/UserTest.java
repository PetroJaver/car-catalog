package com.implemica.application.util.model;

import com.implemica.model.entity.User;
import com.implemica.model.enums.Role;
import com.implemica.model.enums.Status;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {
    @Test
    public void testGetAllFieldUser(){
        User user = new User(1L,"test@test.email"
                ,"$2a$12$qV1GJcrL.vnDhY4G5c1v..V9w6tRxDncJqhCzMTzOlChn/927.8oW"
                ,"Test","Test", Role.ADMIN, Status.ACTIVE);

        assertEquals(Long.valueOf(1L),user.getId());
        assertEquals("test@test.email",user.getEmail());
        assertEquals("$2a$12$qV1GJcrL.vnDhY4G5c1v..V9w6tRxDncJqhCzMTzOlChn/927.8oW",user.getPassword());
        assertEquals("Test",user.getFirstName());
        assertEquals("Test",user.getLastName());
        assertEquals(Role.ADMIN,user.getRole());
        assertEquals(Status.ACTIVE,user.getStatus());
    }
}
