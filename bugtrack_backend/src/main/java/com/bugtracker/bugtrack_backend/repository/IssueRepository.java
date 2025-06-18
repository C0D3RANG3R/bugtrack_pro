package com.bugtracker.bugtrack_backend.repository;

import com.bugtracker.bugtrack_backend.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByProjectId(Long projectId);
    List<Issue> findByAssigneeId(Long assigneeId);

    @Query("SELECT i.status, COUNT(i) FROM Issue i GROUP BY i.status")
    List<Object[]> countByStatusGroupRaw();

    @Query("SELECT i.priority, COUNT(i) FROM Issue i GROUP BY i.priority")
    List<Object[]> countByPriorityGroupRaw();

    List<Issue> findByProjectIdAndStatus(Long projectId, String status);

    @Query("SELECT i FROM Issue i WHERE " +
           "LOWER(i.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(i.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Issue> search(@Param("query") String query);
}
