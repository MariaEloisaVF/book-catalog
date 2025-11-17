package com.example.bookcatalog.webinterface.controller;

import com.example.bookcatalog.application.service.BookService;
import com.example.bookcatalog.domain.model.Book;
import com.example.bookcatalog.webinterface.dto.BookRequest;
import com.example.bookcatalog.webinterface.dto.BookResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")

public class BookController {
    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest req){
        Book book = new Book(
                null,
                req.getTitle(),
                req.getAuthor(),
                req.getIsbn(),
                req.getYear(),
                req.getQuantity()
        );
        Book saved = service.create(book);

        return ResponseEntity
                .created(URI.create("/api/books" + saved.getId()))
                .body(toResponse(saved));
    }

    @GetMapping
    public List<BookResponse> findAll(){
        return service.listAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public BookResponse getById(@PathVariable Long id){
        return toResponse(service.getById(id));
    }

    @GetMapping("/isbn/{isbn}")
    public BookResponse findByIsbn(@PathVariable String isbn) {
        return toResponse(service.findByIsbn(isbn));
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest req) {

        Book updated = new Book(
                id,
                req.getTitle(),
                req.getAuthor(),
                req.getIsbn(),
                req.getYear(),
                req.getQuantity()
        );

        return toResponse(service.update(id, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private BookResponse toResponse(Book b) {
        return new BookResponse(
                b.getId(),
                b.getTitle(),
                b.getAuthor(),
                b.getIsbn(),
                b.getYear(),
                b.getQuantity()
        );
    }
}
