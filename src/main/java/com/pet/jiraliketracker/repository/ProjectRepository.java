package com.pet.jiraliketracker.repository;

import com.pet.jiraliketracker.model.Project;
import com.pet.jiraliketracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOwner(User owner);
    List<Project> findByMembersContaining(User user);
    boolean existsByOwnerAndName(User owner, String name);
}
