package com.skcc.servicerouter.controller;

import com.skcc.servicerouter.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("token")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("validate")
    public Map<String, Boolean> validateToken(@RequestParam("token") String token) {
        final boolean validated = tokenService.validate(token);
        return new HashMap<String, Boolean>() {{
            put("validated", validated);
        }};
    }
}
