package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.TheaterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterTypeRepository extends JpaRepository<TheaterType, Long> {

    TheaterType findTheaterTypeByName(String name);
}
