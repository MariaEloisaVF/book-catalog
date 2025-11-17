package com.example.bookcatalog.webinterface.dto;

public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer year;
    private Integer quantity;

    public BookResponse(Long id, String title, String author, String isbn, Integer year, Integer quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
