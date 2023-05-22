package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Movie;
import com.dbdesign.scv.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    Showtime findShowtimeById(Long showtimeId);
    boolean existsByMovie(Movie movie);
}
