package app.javacode.controller;

import app.javacode.model.Book;
import app.javacode.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable long id) {
        return bookService.getBookById(id) != null ? ResponseEntity.ok(bookService.getBookById(id)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public void createBook(@RequestBody Book book) {
        bookService.createBook(book);
    }

    @PutMapping("/{id}")
    public void updateBook(@PathVariable long id, @RequestBody Book book) {
        book.setId(id);
        bookService.updateBook(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable long id) {
        bookService.deleteBook(bookService.getBookById(id));
    }
}
