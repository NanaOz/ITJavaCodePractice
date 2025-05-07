package app.javacode.repository;

import app.javacode.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepository {
    private final JdbcTemplate jdbcTemplate;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Book save(Book book) {
        if (book.getId() == null) {
            Long id = jdbcTemplate.queryForObject(
                    "INSERT INTO books(title, author, publication_year) VALUES (?, ?, ?) RETURNING id",
                    Long.class,
                    book.getTitle(), book.getAuthor(), book.getPublicationYear());
            book.setId(id);
            return book;
        } else {
            update(book);
            return book;
        }
    }

    public void update(Book book) {
        jdbcTemplate.update("UPDATE books SET title = ?, author = ?, publication_year = ? WHERE id = ?",
                book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getId());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM books WHERE id = ?", id);
    }

    public Book findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM books WHERE id = ?", new Object[]{id}, (rs, rowNum) ->
                        new Book(rs.getLong("id"),
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getInt("publication_year")));
    }

    public List<Book> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM books", (rs, rowNum) ->
                        new Book(rs.getLong("id"),
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getInt("publication_year")));
    }
}
