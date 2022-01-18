package ibf2021.SSFAssessment.controllers;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ibf2021.SSFAssessment.models.Book;
import ibf2021.SSFAssessment.services.BookService;

@Controller
@RequestMapping(path = "/book", produces = MediaType.TEXT_HTML_VALUE)
public class BookController {

    private final Logger logger = Logger.getLogger(BookController.class.getName());

    @Autowired
    BookService bookSvc;

    @GetMapping(path = "{bookId}")
    public String getBook(@PathVariable String bookId, Model model) {
        logger.info("bookId that's parsed in -->" + bookId);
        // use the book ID to GET book details to be displayed in book.html
        // by passing in BookSvc.getBook(bookId)
        Book bookDetails = bookSvc.getBook(bookId);
        model.addAttribute("bookDetails", bookDetails);

        return "book";

    }
}
