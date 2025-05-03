package uga.edu.cs.finalProjectDBMS.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uga.edu.cs.finalProjectDBMS.services.UserService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    public ModelAndView registerPage(@RequestParam(name = "error", required = false) String error) {
        ModelAndView mv = new ModelAndView("register");
        mv.addObject("error", error);
        return mv;
    }

    @PostMapping
    public String register(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String firstName,
            @RequestParam String lastName) {

        if (!password.equals(confirmPassword)) {
            return "redirect:/register?error=" + URLEncoder.encode("Passwords do not match", StandardCharsets.UTF_8);
        }

        if (password.trim().length() < 3) {
            return "redirect:/register?error=" + URLEncoder.encode("Password too short", StandardCharsets.UTF_8);
        }

        try {
            boolean registered = userService.registerUser(email, password, firstName, lastName);
            if (registered) {
                return "redirect:/login";
            } else {
                return "redirect:/register?error=" + URLEncoder.encode("Registration failed", StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            return "redirect:/register?error=" + URLEncoder.encode("Error: " + e.getMessage(), StandardCharsets.UTF_8);
        }
    }
}
