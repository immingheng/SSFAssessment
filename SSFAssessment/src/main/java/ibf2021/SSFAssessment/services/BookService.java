package ibf2021.SSFAssessment.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ibf2021.SSFAssessment.models.Book;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class BookService {

    private final Logger logger = Logger.getLogger(BookService.class.getName());

    // Method to call in SearchController to search for the list of books (Max 20
    // indicated in queryParam)
    public List<Book> search(String query) {
        // if query contains whitespaces, trim them and change spaces to +
        String normalisedString = normaliseString(query);
        List<Book> titles = new LinkedList<>();
        String uri = "http://openlibrary.org/search.json";
        final String url = UriComponentsBuilder.fromUriString(uri)
                .queryParam("title", normalisedString)
                .queryParam("limit", 20)
                .toUriString();
        logger.info("API call's URL with query parameters -->" + url);
        RequestEntity<Void> req = RequestEntity.get(url).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);
        // logger.info("API response (FULL JSON) --> " + resp);

        // Extract relevant data from the FULL JSON to get title, id and 1st ISBN (Cover
        // page rendering)
        try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject jo = reader.readObject();
            // logger.info("JSON obtained from the response ---> " + jo);
            JsonArray docs = jo.getJsonArray("docs");
            // logger.info("DOCS ARRAY that contains title and id --> " + docs);

            for (int i = 0; i < docs.size(); i++) {

                // for each object in the JsonArray, get the key, title and optional<String>
                // ISBN
                String id = docs.get(i).asJsonObject().getString("key");
                // this will return /works/<works ID> therefore replace works with book to pass
                // as href in html
                logger.info("key extracted from one array object --> " + id);
                String worksID = id.replaceAll("works", "book");
                // int LastSlashIndexid = id.lastIndexOf("/");
                // String worksID = id.substring(LastSlashIndexid + 1);
                logger.info("worksID --> " + worksID);
                String title = docs.get(i).asJsonObject().getString("title");
                logger.info("title extracted from one array object --> " + title);

                Book book = new Book();
                book.setWorksID(worksID);
                book.setTitle(title);
                titles.add(book);
            }
            // logger.info("titles -->" + titles.toString());

        } catch (IOException IOException) {
            IOException.printStackTrace();
        } catch (RestClientException RCException) {
            RCException.printStackTrace();
        }

        return titles;
    }

    public String normaliseString(String query) {
        logger.info("Query from form --> " + query);
        if (query.contains(" ")) {
            query = query.trim().replaceAll("\\s", "+");
            logger.info("Replaced query's whitespaces with + --> " + query);
        }
        return query;
    }

    public Book getBook(String bookId) {
        String baseURI = "https://openlibrary.org/works/";
        String url = "%s%s%s".formatted(baseURI, bookId, ".json");
        logger.info("URL TO GET BOOK DETALS BASED ON WORK ID --> " + url);
        Book book = new Book();
        RequestEntity<Void> req = RequestEntity.get(url).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);
        // resp will be the entire JSON that has to be manipulated to extract title,
        // description, covers (Used to generate thumbnail) and excerpts accordingly.
        // logger.info("resp --> " + resp);
        try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject bookData = reader.readObject();
            logger.info("bookData --> " + bookData);
            String title = bookData.getString("title");
            logger.info("Book title -->" + title);
            String coverID = bookData.getJsonArray("covers").get(0).toString();
            String coverURL = "https://covers.openlibrary.org/b/id/";
            String formatExtension = "-L.jpg";
            String thumbnailURL = "%s%s%s".formatted(coverURL, coverID, formatExtension);
            logger.info("ThumbnailURL-->" + thumbnailURL);
            String description = (bookData.getString("description", null));
            logger.info("Book description-->" + description);
            String excerpt;
            Optional<JsonArray> excerpts = Optional.ofNullable(bookData.getJsonArray("excerpts"));
            if (excerpts.isPresent()) {
                JsonArray excerptsPresent = excerpts.get();
                excerpt = excerptsPresent.get(0).asJsonObject().getString("excerpt", "");
            } else {
                excerpt = null;
            }
            logger.info("Excerpt -->" + excerpt);
            book.setTitle(title);
            book.setThumbnailURL(thumbnailURL);
            book.setDescription(description);
            book.setExcerpt(excerpt);

        } catch (IOException IOException) {
            IOException.printStackTrace();
        } catch (RestClientException RCException) {
            RCException.printStackTrace();
        }

        return book;
    }

}
