package ibf2021.SSFAssessment.repositories;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ibf2021.SSFAssessment.models.Book;

@Repository
public class BookRepository {

    @Autowired
    @Qualifier("Redis_Config")
    private RedisTemplate<String, Object> template;

    public void addToRedis(String bookID, Book book) {
        template.opsForValue().set(bookID, book, Duration.ofMinutes(10));
    }

    public Optional<Book> getBook(String bookID) {
        return Optional.ofNullable((Book) template.opsForValue().get(bookID));

    }

}
