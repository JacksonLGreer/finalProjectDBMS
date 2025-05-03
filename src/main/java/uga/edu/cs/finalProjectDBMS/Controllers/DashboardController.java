package uga.edu.cs.finalProjectDBMS.Controllers;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import uga.edu.cs.finalProjectDBMS.Models.BookDto;
import uga.edu.cs.finalProjectDBMS.Models.User;
import uga.edu.cs.finalProjectDBMS.services.BookService;
import uga.edu.cs.finalProjectDBMS.services.UserService;

@Controller
public class DashboardController {

    private final UserService userService;
    private final BookService bookService;
    public DashboardController(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }
    @GetMapping("/dashboard")
    public ModelAndView dashboard(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return new ModelAndView("redirect:/auth/login");
        }

        List<BookDto> books = bookService.getRandomBooksWithDetails();
        ModelAndView mv = new ModelAndView("dashboard");
        mv.addObject("user", user);
        mv.addObject("books", books);
        return mv;
    }
}
