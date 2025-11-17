package com.example.bookcatalog.application;

import com.example.bookcatalog.application.exceptions.DuplicateISBNException;
import com.example.bookcatalog.application.exceptions.BookNotFoundException;
import com.example.bookcatalog.application.service.BookService;
import com.example.bookcatalog.domain.model.Book;
import com.example.bookcatalog.domain.repository.BookRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    private BookRepository repository;
    private BookService service;

    @BeforeEach
    void setup() {
        repository = mock(BookRepository.class);
        service = new BookService(repository);
    }

    @Test
    void createBooksSuccessfully() {
        Book book = new Book(null, "Book A", "Author A", "123", 2002, 10);

        when(repository.existsByIsbn("123")).thenReturn(false);
        when(repository.save(book))
                .thenReturn(new Book(1L, "Book A", "Author A", "123", 2002, 10));

        Book saved = service.create(book);

        assertNotNull(saved.getId());
        assertEquals("Book A", saved.getTitle());
        verify(repository).save(book);
    }

    @Test
    void doNotRepeatISBN() {
        Book book = new Book(null, "Book A", "Author A", "123", 2002, 10);

        when(repository.existsByIsbn("123")).thenReturn(true);

        assertThrows(DuplicateISBNException.class,
                () -> service.create(book));
    }

    @Test
    void findAllBooks() {
        when(repository.findAll()).thenReturn(List.of(
                new Book(1L, "A", "Author", "111", 2021, 5),
                new Book(2L, "B", "Author", "222", 2022, 3)
        ));

        List<Book> books = service.listAll();

        assertEquals(2, books.size());
        verify(repository).findAll();
    }

    @Test
    void exceptionIdNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> service.getById(99L));
    }

    @Test
    void getByIdSuccessfully() {
        Book book = new Book(1L, "A", "Author", "111", 2021, 5);

        when(repository.findById(1L)).thenReturn(Optional.of(book));

        Book found = service.getById(1L);

        assertEquals("A", found.getTitle());
        assertEquals(1L, found.getId());
    }

    @Test
    void getByISBNSuccessfully() {
        Book book = new Book(1L, "A", "Author", "111", 2021, 5);

        when(repository.findByIsbn("111")).thenReturn(Optional.of(book));

        Book found = service.findByIsbn("111");

        assertEquals("A", found.getTitle());
    }

    @Test
    void exceptionISBNNotFound() {
        when(repository.findByIsbn("999")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> service.findByIsbn("999"));
    }

    @Test
    void updateBookSuccessfully() {
        Book original = new Book(1L, "A", "Author A", "111", 2021, 5);
        Book updated = new Book(1L, "New", "Author B", "111", 2020, 8);

        when(repository.findById(1L)).thenReturn(Optional.of(original));
        when(repository.existsByIsbn("111")).thenReturn(false);
        when(repository.save(ArgumentMatchers.any(Book.class))).thenReturn(updated);

        Book result = service.update(1L, updated);

        assertEquals("New", result.getTitle());
    }

    @Test
    void doNotUpdateIfIdNotFound() {
        Book updated = new Book(1L, "New", "Author B", "111", 2020, 8);

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> service.update(1L, updated));
    }

    @Test
    void deleteBookSuccessfully() {
        Book book = new Book(1L, "A", "Author A", "111", 2021, 5);

        when(repository.findById(1L)).thenReturn(Optional.of(book));

        service.delete(1L);

        verify(repository).delete(book);
    }

    @Test
    void doNotDeleteIfBookNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> service.delete(1L));
    }
}