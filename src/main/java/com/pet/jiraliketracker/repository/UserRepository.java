package com.pet.jiraliketracker.repository;

import com.pet.jiraliketracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findAllByEmailIn(List<String> emails);
    boolean existsByEmail(String email);
    //@Query("SELECT m.id FROM Project p JOIN p.members m WHERE p.id = :projectId")
//    List<Long> findMemberIdsByProjectId(@Param("projectId") Long projectId);
//    List<String > findMemberEmailsByProjectId(@Param("projectId") Long projectId);

}
