package com.example.demo.repository;

import com.example.demo.model.Anunt;
import com.example.demo.model.Eveniment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface EvenimentRepository extends JpaRepository<Eveniment, Long> {


    @Query("SELECT u FROM Eveniment u WHERE u.user.id = :userId")
    List<Eveniment> findByUserId(Long userId);

    @Query("SELECT u FROM Eveniment u WHERE lower(u.name) LIKE lower(concat('%', :name, '%'))")
    List<Eveniment> findByNameContaining(String name);
}