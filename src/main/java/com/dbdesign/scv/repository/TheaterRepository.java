package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    Theater findTheaterById(Long id);
}
