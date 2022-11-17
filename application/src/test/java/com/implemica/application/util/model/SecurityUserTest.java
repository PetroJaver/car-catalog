package com.implemica.application.util.model;

import com.implemica.security.SecurityUser;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class SecurityUserTest {
    @Test
    public void testAllGettersSecuritydUser() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();

        SecurityUser securityUser = new SecurityUser("Test","Test", authorities,true);
        assertEquals(securityUser.getPassword(),"Test");
        assertEquals(securityUser.getUsername(),"Test");
        assertEquals(securityUser.getAuthorities(),authorities);
        assertEquals(true,securityUser.isActive());
        assertEquals(true,securityUser.isAccountNonExpired());
        assertEquals(true,securityUser.isAccountNonLocked());
        assertEquals(true,securityUser.isCredentialsNonExpired());
        assertEquals(true,securityUser.isEnabled());
    }
}
