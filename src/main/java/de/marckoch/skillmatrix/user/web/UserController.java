package de.marckoch.skillmatrix.user.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/user")
    public String userProfile() {
        return "user";
    }
}
