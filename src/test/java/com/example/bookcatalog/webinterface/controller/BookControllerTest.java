package com.example.bookcatalog.webinterface.controller;

import com.example.bookcatalog.application.service.BookService;
import com.example.bookcatalog.application.exceptions.BookNotFoundException;
import com.example.bookcatalog.domain.model.Book;
import com.example.bookcatalog.webinterface.dto.BookRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookControllerTest {
    private MockMvc mockMvc;
    private BookService service;
    private ObjectMapper mapper;

    @BeforeEach
    void setup(){
        service = Mockito.mock(BookService.class);
        BookController controller = new BookController(service);

        mockMvc = standaloneSetup(controller).build();
        mapper = new ObjectMapper();
    }

    @Test
    void createBooksSuccessfully() throws Exception{
        Book book = new Book(1L, "Title", "Author A", "111", 2021, 5);
        BookRequest req = new BookRequest();
        req.setTitle("Title");
        req.setAuthor("Author A");
        req.setIsbn("123");
        req.setYear(2021);
        req.setQuantity(5);

        when(service.create(any(Book.class))).thenReturn(book);
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/books/1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void listAllBooks() throws Exception{
        when(service.listAll()).thenReturn(
                List.of(
                        new Book(1L, "Title A", "Author A", "111", 2021, 5),
                        new Book(2L, "Title B", "Author B", "222", 2023, 9)
                )
        );

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title A"))
                .andExpect(jsonPath("$[1].title").value("Title B"));
    }

    @Test
    void getBookByIdSuccessfully() throws Exception{
        when(service.getById(1L))
                .thenReturn(new Book(1L, "Title A", "Author A", "111", 2021, 5));
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("111"));
    }

    @Test
    void getBookByISBN() throws Exception{
        when(service.findByIsbn("111"))
                .thenReturn(new Book(1L, "Title A", "Author A", "111", 2021, 5));
        mockMvc.perform(get("/api/books/isbn/111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title A"));
    }

    @Test
    void updateBookSuccessfully() throws Exception{
        Book updated = new Book(1L, "New", "Author B", "111", 2020, 12);

        BookRequest req = new BookRequest();
        req.setTitle("New");
        req.setAuthor("Author B");
        req.setIsbn("111");
        req.setYear(2020);
        req.setQuantity(12);

        when(service.update(eq(1L), any(Book.class))).thenReturn(updated);
        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New"));
    }

    @Test
    void deleteBookSuccessfully() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void returnNotFound() throws Exception{
        when(service.getById(99L))
                .thenThrow(new BookNotFoundException("Book not found"));
        mockMvc.perform(get("/api/books/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBookNotFound() throws Exception{
        Mockito.doThrow(new BookNotFoundException("Not found"))
                .when(service).delete(99L);

        mockMvc.perform(delete("/api/books/99"))
                .andExpect(status().isNotFound());
    }
}
