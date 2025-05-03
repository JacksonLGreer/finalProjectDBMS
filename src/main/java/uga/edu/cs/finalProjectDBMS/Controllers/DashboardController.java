package uga.edu.cs.finalProjectDBMS.Controllers;

import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import uga.edu.cs.finalProjectDBMS.Models.User;
import uga.edu.cs.finalProjectDBMS.services.UserService;

@Controller
public class DashboardController {

    private final UserService userService;
    public DashboardController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/dashboard")
    public ModelAndView dashboard(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return new ModelAndView("redirect:/auth/login");
        }

        ModelAndView mv = new ModelAndView("dashboard");
        mv.addObject("user", user);
        return mv;
    }
}
