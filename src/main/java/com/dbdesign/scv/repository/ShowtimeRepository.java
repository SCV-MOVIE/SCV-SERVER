package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Movie;
import com.dbdesign.scv.entity.Showtime;
import com.dbdesign.scv.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    Showtime findShowtimeById(Long showtimeId);
    boolean existsByMovie(Movie movie);
    List<Showtime> findAllByTheater(Theater theater);
}
