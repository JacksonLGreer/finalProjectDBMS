package uga.edu.cs.finalProjectDBMS.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uga.edu.cs.finalProjectDBMS.services.BookService;
import uga.edu.cs.finalProjectDBMS.Models.BookDto;

@Controller
@RequestMapping("/book")
public class BookInfoController {

    private final BookService bookService;

    @Autowired
    public BookInfoController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/info")
    public ModelAndView getBookInfo(@RequestParam("bookId") int bookId) {
        BookDto book = bookService.getBookById(bookId);

        if (book == null) {
            // Optional: redirect to error page or dashboard if book not found
            return new ModelAndView("redirect:/dashboard");
        }

        ModelAndView mv = new ModelAndView("book_info");
        mv.addObject("bookId", bookId);
        mv.addObject("title", book.getTitle());
        mv.addObject("publicationYear", book.getPublicationYear());
        mv.addObject("publisherName", book.getPublisherName());
        mv.addObject("authors", book.getAuthors());

        return mv;
    }
}