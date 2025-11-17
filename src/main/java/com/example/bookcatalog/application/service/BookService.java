package com.example.bookcatalog.application.service;

import com.example.bookcatalog.application.exceptions.BookNotFoundException;
import com.example.bookcatalog.application.exceptions.DuplicateISBNException;
import com.example.bookcatalog.domain.model.Book;
import com.example.bookcatalog.domain.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository repo;

    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Book create(Book book) {
        if (repo.existsByIsbn(book.getIsbn())) {
            throw new DuplicateISBNException("ISBN already exists");
        }
        return repo.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> listAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Book getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
    }

    @Transactional(readOnly = true)
    public Book findByIsbn(String isbn) {
        return repo.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book with ISBN " + isbn + " not found"));
    }

    @Transactional
    public Book update(Long id, Book updatedBook) {
        Book existing = repo.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (!existing.getIsbn().equals(updatedBook.getIsbn())
                && repo.existsByIsbn(updatedBook.getIsbn())) {
            throw new IllegalArgumentException("Changing the ISBN is not allowed");
        }

        existing.setTitle(updatedBook.getTitle());
        existing.setAuthor(updatedBook.getAuthor());
        existing.setIsbn(updatedBook.getIsbn());
        existing.setYear(updatedBook.getYear());
        existing.setQuantity(updatedBook.getQuantity());

        return repo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Book book = repo.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
        repo.delete(book);
    }
}