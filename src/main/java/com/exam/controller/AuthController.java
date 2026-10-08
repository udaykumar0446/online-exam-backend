package com.exam.controller;

import com.exam.model.User;
import com.exam.repo.UserRepo;
import com.exam.security.JwtUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController @RequestMapping("/api/auth") @Validated
public class AuthController {
    record RegisterReq(@NotBlank String name, @Email @NotBlank String email, @Size(min = 6) String password) {}
    record LoginReq(String email, String password) {}

    private final UserRepo users; private final PasswordEncoder enc; private final JwtUtil jwt;
    public AuthController(UserRepo u, PasswordEncoder e, JwtUtil j) { users = u; enc = e; jwt = j; }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody @jakarta.validation.Valid RegisterReq r) {
        if (users.findByEmail(r.email()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        User u = new User();
        u.setName(r.name()); u.setEmail(r.email()); u.setPassword(enc.encode(r.password())); u.setRole("STUDENT");
        users.save(u);
        return response(u);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginReq r) {
        User u = users.findByEmail(r.email()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (!enc.matches(r.password(), u.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        return response(u);
    }

    private Map<String, Object> response(User u) {
        return Map.of("token", jwt.generate(u.getEmail(), u.getRole()), "name", u.getName(), "role", u.getRole());
    }
}
