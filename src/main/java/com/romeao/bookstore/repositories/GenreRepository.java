package com.romeao.bookstore.repositories;

import com.romeao.bookstore.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {}
