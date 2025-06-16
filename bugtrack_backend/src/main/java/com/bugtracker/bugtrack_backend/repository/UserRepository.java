package com.bugtracker.bugtrack_backend.repository;

import com.bugtracker.bugtrack_backend.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
