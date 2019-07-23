package com.romeao.bookstore.domain;

import com.romeao.bookstore.domain.builders.BookBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Book extends BaseEntity {

    @NotBlank
    private String name;

    @NotBlank
    private String publisher;

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal price;

    @ManyToMany
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    public static BookBuilder builder() {
        return new BookBuilder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Book book = (Book) o;
        return Objects.equals(getId(), book.getId()) &&
                Objects.equals(name, book.name) &&
                Objects.equals(publisher, book.publisher) &&
                Objects.equals(price, book.price) &&
                authors.size() == book.authors.size() &&
                authors.containsAll(book.authors) &&
                genres.size() == book.genres.size() &&
                genres.containsAll(book.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, publisher, price, authors, genres);
    }
}
