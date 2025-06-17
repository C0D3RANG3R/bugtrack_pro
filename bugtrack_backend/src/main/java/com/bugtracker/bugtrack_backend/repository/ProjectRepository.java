package com.bugtracker.bugtrack_backend.repository;

import com.bugtracker.bugtrack_backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
