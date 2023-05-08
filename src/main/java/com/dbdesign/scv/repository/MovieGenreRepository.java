package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Movie;
import com.dbdesign.scv.entity.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, Long> {

    List<MovieGenre> findMovieGenresByMovie(Movie movie);
}
