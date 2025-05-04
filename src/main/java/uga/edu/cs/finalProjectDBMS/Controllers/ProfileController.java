package uga.edu.cs.finalProjectDBMS.Controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uga.edu.cs.finalProjectDBMS.Models.User;
import uga.edu.cs.finalProjectDBMS.services.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView showProfile(HttpSession session) {
        User user = userService.getLoggedInUser();
        if (user == null) return new ModelAndView("redirect:/login");

        ModelAndView mv = new ModelAndView("profile");
        mv.addObject("user", user);
        return mv;
    }

    @PostMapping
    public String updateProfile(@RequestParam String firstName,
                                 @RequestParam String lastName,
                                 @RequestParam String email) {
        try {
            userService.updateUser(firstName, lastName, email);
            return "redirect:/profile";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/profile?error=UpdateFailed";
        }
    }
}
