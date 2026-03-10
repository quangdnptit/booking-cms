package com.demo.booking_cms.repository;

import com.demo.booking_cms.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, UUID> {
}
