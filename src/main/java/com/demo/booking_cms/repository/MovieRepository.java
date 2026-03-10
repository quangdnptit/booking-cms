package com.demo.booking_cms.repository;

import com.demo.booking_cms.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {

    List<Movie> findByTitleContainingIgnoreCase(String title);

    List<Movie> findByGenre(String genre);
}
