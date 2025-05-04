package uga.edu.cs.finalProjectDBMS.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import uga.edu.cs.finalProjectDBMS.Models.User;
import uga.edu.cs.finalProjectDBMS.services.UserService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ModelAndView loginPage(@RequestParam(name = "error", required = false) String error) {
        ModelAndView mv = new ModelAndView("login");
        userService.unAuthenticate(); // optional: reset session or clear auth
        mv.addObject("error", error);
        return mv;
    }

    // @PostMapping
    // public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
    //     try {
    //         if (userService.authenticate(email, password)) {
    //             session.setAttribute("user", userService.getLoggedInUser());
    //             return "redirect:/dashboard";
    //         } else {
    //             return "redirect:/login?error=" + URLEncoder.encode("Invalid credentials", StandardCharsets.UTF_8);
    //         }
    //     } catch (Exception e) {
    //         return "redirect:/login?error=" + URLEncoder.encode("Login error: " + e.getMessage(), StandardCharsets.UTF_8);
    //     }
    // }
    @PostMapping
public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
    try {
        if (userService.authenticate(email, password)) {
            User user = userService.getLoggedInUser();
            session.setAttribute("user", user);               // optional
            session.setAttribute("userId", user.getUserId()); // ✅ required for loan
            System.out.println("Logged in as userId = " + user.getUserId());
            return "redirect:/dashboard";
        } else {
            return "redirect:/login?error=" + URLEncoder.encode("Invalid credentials", StandardCharsets.UTF_8);
        }
    } catch (Exception e) {
        return "redirect:/login?error=" + URLEncoder.encode("Login error: " + e.getMessage(), StandardCharsets.UTF_8);
    }
}

}
