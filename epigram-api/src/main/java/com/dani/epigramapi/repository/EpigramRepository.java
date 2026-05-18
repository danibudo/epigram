package com.dani.epigramapi.repository;

import com.dani.epigramapi.model.Epigram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface EpigramRepository extends JpaRepository<Epigram, Integer> {

    @Query(value = "SELECT * FROM epigrams ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Epigram> findRandom();
}