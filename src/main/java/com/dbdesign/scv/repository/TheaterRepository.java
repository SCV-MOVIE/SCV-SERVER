package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    Theater findTheaterById(Long theaterId);
    boolean existsById(Long theaterId);
}
