package com.example.bookcatalog.domain.repository;

import com.example.bookcatalog.domain.model.Book;
//import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface BookRepository{
    Optional<Book> findById(Long id);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findAll();
    Book save(Book book);
    void delete(Book book);
    boolean existsByIsbn(String isbn);
}
