package com.example.bookcatalog.integration;

import com.example.bookcatalog.domain.model.Book;
import com.example.bookcatalog.domain.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        repository.findAll().forEach(repository::delete);
    }

    @Test
    void createBook() throws Exception{
        Book book = new Book(null, "Title A", "Author A", "111", 2023, 5);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Title A")));
    }

    @Test
    void getBookById() throws Exception{
        Book saved = repository.save(new Book(null, "Title B", "Author B", "222", 2023, 5));

        mockMvc.perform(get("/api/books" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Title B")));
    }

    @Test
    void getByISBN() throws Exception{
        Book saved = repository.save(new Book(null, "Title C", "Author C", "333", 2023, 5));

        mockMvc.perform(get("/api/books/isbn/333"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author", is("Author C")));
    }

    @Test
    void updateBook() throws Exception{
        Book saved = repository.save(new Book(null, "Title D", "Author D", "444", 2023, 5));
        saved.setTitle("New");

        mockMvc.perform(put("/api/books/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New")));
    }

    @Test
    void deleteBook() throws Exception{
        Book saved = repository.save(new Book(null, "Title E", "Author E", "555", 2023, 5));

        mockMvc.perform(delete("/api/books/" + saved.getId()))
                .andExpect(status().isNoContent());
    }
}
