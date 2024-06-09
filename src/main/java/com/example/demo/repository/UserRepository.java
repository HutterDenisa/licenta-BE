package com.example.demo.repository;

import com.example.demo.model.Anunt;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE lower(u.username) LIKE lower(concat('%', :name, '%'))")
    List<User> findByNameContaining(String name);
}
