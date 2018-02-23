package com.skcc.servicerouter.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @Test
    public void validate_true() {
        String token = tokenService.generate();
        boolean result = tokenService.validate(token);
        assertEquals(true, result);
    }

    @Test
    public void generate() {
        String token = tokenService.generate();
        assertNotNull(token);
    }
}