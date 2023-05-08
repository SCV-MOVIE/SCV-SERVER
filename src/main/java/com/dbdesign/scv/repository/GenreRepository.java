package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Genre findGenreByName(String name);
}
