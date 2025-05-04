package uga.edu.cs.finalProjectDBMS.Controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import uga.edu.cs.finalProjectDBMS.Models.User;
import uga.edu.cs.finalProjectDBMS.services.UserService;

@Controller
public class LoanController {

    @Autowired
    private UserService userService;

    @PostMapping("/loan")
    public String loanBook(@RequestParam int bookId, HttpSession session) {
        //Integer userId = (Integer) session.getAttribute("userId");
        User user = (User) session.getAttribute("user");
        Integer userId = user != null ? user.getUserId() : null;

        if (userId == null) {
            return "redirect:/dashboard?loan=not_logged_in";
        }

        try {
            boolean success = userService.loanBookToUser(bookId, userId);
            System.out.println("Loan success: " + success);

            return "redirect:/dashboard?loan=" + (success ? "success" : "failed");
        } catch (Exception e) {
            e.printStackTrace();

            return "redirect:/dashboard?loan=error";
        }
    }
}
