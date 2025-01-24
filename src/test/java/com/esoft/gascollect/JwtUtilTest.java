package com.esoft.gascollect;

import com.esoft.gascollect.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

//    @Test
//    public void testGenerateAndValidateToken() {
//        String email = "testUser";
//
//        // Generate token
//        String token = jwtUtil.generateToken(email, Map.of());
//
//        // Assertions
//        assertNotNull(token, "Token should not be null");
//        assertEquals(email, jwtUtil.extractEmail(token), "Extracted username should match the original");
//        assertTrue(jwtUtil.validateToken(token, e), "Token should be valid for the given username");
//    }
}
