package uga.edu.cs.finalProjectDBMS.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uga.edu.cs.finalProjectDBMS.services.BookService;
import uga.edu.cs.finalProjectDBMS.Models.BookDto;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final BookService bookService;

    @Autowired
    public SearchController(BookService bookService) {
        this.bookService = bookService;
    }

    // Show the search form
    @GetMapping
    public ModelAndView searchPage() {
        return new ModelAndView("search");
    }

    // Handle the search results
    @GetMapping("/results")
    public ModelAndView searchResults(@RequestParam("query") String query,
                                      @RequestParam("type") String type) {

        List<BookDto> books;
        if ("author".equalsIgnoreCase(type)) {
            books = bookService.searchBooksByAuthor(query);
        } else {
            books = bookService.searchBooksByTitle(query);
        }

        ModelAndView mv = new ModelAndView("search");
        mv.addObject("query", query);
        mv.addObject("results", true);
        mv.addObject("books", books);
        return mv;
    }
}