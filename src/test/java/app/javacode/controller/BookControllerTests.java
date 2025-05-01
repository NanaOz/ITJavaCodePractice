package app.javacode.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.javacode.model.Author;
import app.javacode.model.Book;
import app.javacode.repository.AuthorRepository;
import app.javacode.repository.BookRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class BookControllerTests {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Book book;
    private Author author;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        author = new Author("John", "Doe");
        author.setId(1L);

        book = new Book("Test Book", 200, LocalDate.now(), author);
        book.setId(1L);
    }

    @Test
    public void getAllBooksShouldReturnPageOfBooks() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(Collections.singletonList(book), pageable, 1);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(book.getId().toString()))
                .andExpect(jsonPath("$.content[0].title").value(book.getTitle()))
                .andExpect(jsonPath("$.content[0].pages").value(book.getPages()));
    }

    @Test
    public void getBookByIdShouldReturnBook() throws Exception {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId().intValue()))
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.pages").value(book.getPages()));
    }

    @Test
    public void getBookByIdShouldReturnBookNotFound() throws Exception {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createBookShouldReturnBadRequest() throws Exception {
        Author authorWithoutId = new Author("John", "Doe");
        Book book = new Book("New Book", 100, LocalDate.now(), authorWithoutId);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateBookShouldReturnBookNotFound() throws Exception {
        Book updatedBook = new Book("New Book", 100, LocalDate.now(), author);

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteBookShouldReturnNoContent() throws Exception {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteBookShouldReturnNotFound() throws Exception {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNotFound());
    }
}
