package com.bugtracker.bugtrack_backend.service;

import com.bugtracker.bugtrack_backend.dto.ProjectDTO;
import com.bugtracker.bugtrack_backend.entity.Project;
import com.bugtracker.bugtrack_backend.entity.User;
import com.bugtracker.bugtrack_backend.repository.ProjectRepository;
import com.bugtracker.bugtrack_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Project createProject(ProjectDTO dto) {
        Project project = mapDtoToProject(new Project(), dto);
        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Long id, ProjectDTO dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));
        mapDtoToProject(project, dto);
        return projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    private Project mapDtoToProject(Project project, ProjectDTO dto) {
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());

        Set<User> users = new HashSet<>();
        if (dto.getUserIds() != null) {
            dto.getUserIds().forEach(userId ->
                userRepository.findById(userId).ifPresent(users::add)
            );
        }
        project.setUsers(users);
        return project;
    }
}
