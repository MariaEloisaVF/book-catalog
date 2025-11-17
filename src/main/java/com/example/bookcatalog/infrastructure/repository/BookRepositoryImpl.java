package com.example.bookcatalog.infrastructure.repository;

import com.example.bookcatalog.domain.model.Book;
import com.example.bookcatalog.domain.repository.BookRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryImpl implements BookRepository{
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        List<Book> result = em.createQuery(
                        "SELECT b FROM Book b WHERE b.isbn = :isbn",
                        Book.class)
                .setParameter("isbn", isbn)
                .getResultList();

        return result.stream().findFirst();
    }

    @Override
    public List<Book> findAll() {
        return em.createQuery("SELECT b FROM Book b", Book.class)
                .getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null){
            em.persist(book);
        }
        return em.merge(book);
    }

    @Override
    public void delete(Book book) {
        em.remove(em.contains(book) ? book : em.merge(book));
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        Boolean exists = em.createQuery(
                        "SELECT COUNT(b) > 0 FROM Book b WHERE b.isbn = :isbn",
                        Boolean.class)
                .setParameter("isbn", isbn)
                .getSingleResult();
        return exists;
    }
}
