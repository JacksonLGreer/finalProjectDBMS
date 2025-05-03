package uga.edu.cs.finalProjectDBMS.Controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class HomeController {
    
    public HomeController() {

    }

    @GetMapping
    public ModelAndView webpage() {
        ModelAndView mv = new ModelAndView("home_page");
        return mv;

    }

}
