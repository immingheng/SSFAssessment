package ibf2021.SSFAssessment.controllers;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ibf2021.SSFAssessment.models.Book;
import ibf2021.SSFAssessment.services.BookService;

@Controller
@RequestMapping(path = "/", produces = MediaType.TEXT_HTML_VALUE)
public class SearchController {

    @Autowired
    BookService bookSvc;

    private final static Logger logger = Logger.getLogger(SearchController.class.getName());

    @PostMapping(path = "/search", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String bookSearch(@RequestBody MultiValueMap<String, String> form, Model model) {
        String query = form.getFirst("searchQuery");
        model.addAttribute("query", query);
        logger.info("Query from form ---> " + query);

        // query will then be passed to BookService.search() to perform search
        // the results from the search will be a list of URLs that will be rendered on
        // page "search"
        List<Book> searchTitles = bookSvc.search(query);
        logger.info("Display the list of books --> " + searchTitles);
        model.addAttribute("searchTitles", searchTitles);
        if (searchTitles.isEmpty())
            return "error";

        return "search";
    }

}
