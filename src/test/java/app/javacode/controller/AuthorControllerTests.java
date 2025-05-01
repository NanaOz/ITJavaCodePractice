package app.javacode.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.javacode.model.Author;
import app.javacode.model.Book;
import app.javacode.repository.AuthorRepository;
import app.javacode.repository.BookRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class AuthorControllerTests {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private AuthorController authorController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Author author;
    private Book book;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
        objectMapper = new ObjectMapper();

        author = new Author("John", "Doe");
        author.setId(1L);
    }

    @Test
    public void getAllAuthorsShouldReturnAllAuthors() throws Exception {
        Author author2 = new Author("Janin", "Does");
        author2.setId(2L);
        List<Author> authors = Arrays.asList(author, author2);

        when(authorRepository.findAll()).thenReturn(authors);

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(author.getId()))
                .andExpect(jsonPath("$[0].name").value(author.getName()))
                .andExpect(jsonPath("$[0].surname").value(author.getSurname()))
                .andExpect(jsonPath("$[1].id").value(author2.getId()))
                .andExpect(jsonPath("$[1].name").value(author2.getName()))
                .andExpect(jsonPath("$[1].surname").value(author2.getSurname()));
    }

    @Test
    public void getAuthorByIdShouldReturnAuthor() throws Exception {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        mockMvc.perform(get("/api/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(author.getId()))
                .andExpect(jsonPath("$.name").value(author.getName()))
                .andExpect(jsonPath("$.surname").value(author.getSurname()));
    }

    @Test
    public void createAuthorShouldCreateAuthor() throws Exception {
        Author newAuthor = new Author("Ann", "Doe");

        when(authorRepository.save(any(Author.class))).thenReturn(newAuthor);

        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newAuthor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(newAuthor.getId()))
                .andExpect(jsonPath("$.name").value(newAuthor.getName()))
                .andExpect(jsonPath("$.surname").value(newAuthor.getSurname()));
    }

    @Test
    public void updateAuthorShouldUpdateAuthor() throws Exception {
        Author updatedAuthor  = new Author("Anna", "Poe");
        updatedAuthor.setId(1L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

        mockMvc.perform(put("/api/authors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAuthor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedAuthor.getId()))
                .andExpect(jsonPath("$.name").value(updatedAuthor.getName()))
                .andExpect(jsonPath("$.surname").value(updatedAuthor.getSurname()));
    }

    @Test
    public void updateAuthorShouldReturnNotFound() throws Exception {
        Author updatedAuthor  = new Author("Anna", "Poe");
        updatedAuthor.setId(1L);

        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/authors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAuthor)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteAuthorShouldNoContent() throws Exception {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        mockMvc.perform(delete("/api/authors/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteAuthorShouldNotFoundShouldNotFound() throws Exception {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/authors/1"))
                .andExpect(status().isNotFound());
    }
}
