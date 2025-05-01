package app.javacode.controller;

import app.javacode.model.Author;
import app.javacode.model.Book;
import app.javacode.repository.AuthorRepository;
import app.javacode.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @GetMapping
    public ResponseEntity<Page<Book>> getAllBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isPresent()) {
            return ResponseEntity.ok(optionalBook.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        if (book.getAuthor() == null || book.getAuthor().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author ID is required");
        }

        Author author = authorRepository.findById(book.getAuthor().getId())
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + book.getAuthor().getId()));
        book.setAuthor(author);
        Book savedBook = bookRepository.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Book bookToUpdate = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        bookToUpdate.setTitle(book.getTitle());
        bookToUpdate.setPages(book.getPages());
        bookToUpdate.setPublicationDate(book.getPublicationDate());

        if (book.getAuthor() != null) {
            Author author = authorRepository.findById(book.getAuthor().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
            bookToUpdate.setAuthor(author);
        }

        Book updatedBook = bookRepository.save(bookToUpdate);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable Long id) {
        Book bookToDelete = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        bookRepository.delete(bookToDelete);
        return ResponseEntity.noContent().build();
    }
}
