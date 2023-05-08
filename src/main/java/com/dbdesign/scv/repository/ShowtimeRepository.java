package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Movie;
import com.dbdesign.scv.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    boolean existsByMovie(Movie movie);
}
