package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Movie findMovieById(Long movieId);
}
