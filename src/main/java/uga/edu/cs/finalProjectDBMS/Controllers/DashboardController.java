package uga.edu.cs.finalProjectDBMS.Controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uga.edu.cs.finalProjectDBMS.Models.BookDto;
import uga.edu.cs.finalProjectDBMS.Models.User;
import uga.edu.cs.finalProjectDBMS.services.BookService;
import uga.edu.cs.finalProjectDBMS.services.UserService;

import java.sql.*;
import java.util.*;

@Controller
public class DashboardController {

    private final UserService userService;
    private final BookService bookService;

    // DB connection constants (or move to a config class)
    private static final String JDBC_URL = "jdbc:mysql://localhost:33306/dbms_library";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "mysqlpass";

    public DashboardController(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboard(HttpSession session,
                                  @RequestParam(name = "loan", required = false) String loan) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return new ModelAndView("redirect:/auth/login");
        }

        ModelAndView mv = new ModelAndView("dashboard");
        mv.addObject("user", user);

        // Random books to show
        List<BookDto> books = bookService.getRandomBooksWithDetails();
        mv.addObject("books", books);
        
        mv.addObject("loan", loan != null);

    if (loan != null) {
        mv.addObject("isLoanSuccess", "success".equals(loan));
        mv.addObject("isLoanFailed", "failed".equals(loan));
        mv.addObject("isLoanNotLoggedIn", "not_logged_in".equals(loan));
        mv.addObject("isLoanError", "error".equals(loan));
    }

        // Add current loans
        try {
            mv.addObject("loans", getCurrentLoans(user.getUserId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mv;
    }

    // Helper method to get user’s current loans with book info
    private List<Map<String, Object>> getCurrentLoans(int userId) throws SQLException {
        String sql = """
            SELECT loan.loanDate, loan.returnDate, book.title, book.publicationYear
            FROM loan
            JOIN book ON loan.bookId = book.bookId
            WHERE loan.userId = ?
            ORDER BY loan.loanDate DESC
        """;

        List<Map<String, Object>> result = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("title", rs.getString("title"));
                row.put("publicationYear", rs.getInt("publicationYear"));
                row.put("loanDate", rs.getDate("loanDate"));
                row.put("returnDate", rs.getDate("returnDate"));
                result.add(row);
            }
        }

        return result;
    }
}
