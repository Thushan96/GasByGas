package com.esoft.gascollect.controller;

import com.esoft.gascollect.dto.AuthResponseDTO;
import com.esoft.gascollect.entity.User;
import com.esoft.gascollect.security.JwtUtil;
import com.esoft.gascollect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public AuthController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userService.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body(null);
        }

        User user = userOptional.get();
        if (!userService.validatePassword(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(null);
        }
        String token = jwtUtil.generateToken(user.getEmail(), new HashMap<>());
        String userId=String.valueOf(user.getId());
        String role = user.getRoles().stream()
                .map(r -> r.getName().replace("ROLE_", ""))
                .findFirst()
                .orElse("UNKNOWN");
        AuthResponseDTO responseDTO=new AuthResponseDTO(token, role ,userId);
        return ResponseEntity.ok(responseDTO);
    }
}
