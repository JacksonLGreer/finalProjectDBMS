package uga.edu.cs.finalProjectDBMS.Controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uga.edu.cs.finalProjectDBMS.Models.User;
import uga.edu.cs.finalProjectDBMS.services.UserService;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoanController {

    @Autowired
    private UserService userService;

    @PostMapping("/loan")
    public String loanBook(@RequestParam int bookId, HttpSession session) {
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

    @PostMapping("/loan/return")
    public String returnBook(@RequestParam int loanId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            userService.returnLoan(loanId);
            return "redirect:/loans/current";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/loans/current?error=return_failed";
        }
    }

    @GetMapping("/loans/current")
    public ModelAndView currentLoans(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelAndView mv = new ModelAndView("current-loans");
        mv.addObject("userId", user.getUserId());

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:33306/dbms_library", "root", "mysqlpass")) {

            String sql = """
                SELECT loan.loanId, loan.bookId, book.title, book.publicationYear, loan.loanDate
                FROM loan
                JOIN book ON loan.bookId = book.bookId
                WHERE loan.userId = ? AND loan.returnDate IS NULL
                ORDER BY loan.loanDate DESC
            """;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, user.getUserId());
                try (ResultSet rs = stmt.executeQuery()) {
                    List<Map<String, Object>> loans = new ArrayList<>();

                    while (rs.next()) {
                        Map<String, Object> loan = new HashMap<>();
                        loan.put("loanId", rs.getInt("loanId"));
                        loan.put("bookId", rs.getInt("bookId"));
                        loan.put("title", rs.getString("title"));
                        loan.put("publicationYear", rs.getInt("publicationYear"));
                        loan.put("loanDate", rs.getDate("loanDate"));
                        loans.add(loan);
                    }

                    mv.addObject("loans", loans);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("error", "Could not load current loans.");
        }

        return mv;
    }

    @GetMapping("/loans/history")
    public ModelAndView loanHistory(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelAndView mv = new ModelAndView("loan-history");
        mv.addObject("userId", user.getUserId());

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:33306/dbms_library", "root", "mysqlpass")) {

            String sql = """
                SELECT book.bookId, book.title, book.publicationYear, loan.loanDate, loan.returnDate
                FROM loan
                JOIN book ON loan.bookId = book.bookId
                WHERE loan.userId = ?
                ORDER BY loan.loanDate DESC
            """;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, user.getUserId());
                try (ResultSet rs = stmt.executeQuery()) {
                    List<Map<String, Object>> loans = new ArrayList<>();

                    while (rs.next()) {
                        Map<String, Object> loan = new HashMap<>();
                        loan.put("bookId", rs.getInt("bookId"));
                        loan.put("title", rs.getString("title"));
                        loan.put("publicationYear", rs.getInt("publicationYear"));
                        loan.put("loanDate", rs.getDate("loanDate"));
                        loan.put("returnDate", rs.getDate("returnDate"));
                        loans.add(loan);
                    }

                    mv.addObject("loans", loans);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("error", "Could not load loan history.");
        }

        return mv;
    }
}
