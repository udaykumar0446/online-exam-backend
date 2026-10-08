package com.exam;

import com.exam.model.User;
import com.exam.repo.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class OnlineExamApplication {
    public static void main(String[] args) { SpringApplication.run(OnlineExamApplication.class, args); }

    /** Creates a default admin (admin@exam.com / admin123) on first start. */
    @Bean CommandLineRunner seedAdmin(UserRepo users, PasswordEncoder enc) {
        return a -> {
            if (users.findByEmail("admin@exam.com").isEmpty()) {
                User u = new User();
                u.setName("Administrator"); u.setEmail("admin@exam.com");
                u.setPassword(enc.encode("admin123")); u.setRole("ADMIN");
                users.save(u);
            }
        };
    }
}
