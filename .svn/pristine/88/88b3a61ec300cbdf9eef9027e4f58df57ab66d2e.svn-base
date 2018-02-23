package com.skcc.servicerouter.service;

import com.skcc.servicerouter.utils.TokenGenerator;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {
    private TokenGenerator tokenGenerator = new TokenGenerator();
    private Map<String, String> tokenMap = new ConcurrentHashMap<>();

    public boolean validate(String token) {
        if(tokenMap.containsKey(token)) {
            tokenMap.remove(token);
            return true;
        }
        return false;
    }

    public String generate() {
         String token = tokenGenerator.nextString();
         tokenMap.put(token, token);
         return token;
    }
}
