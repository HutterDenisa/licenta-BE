package com.example.demo.repository;

import com.example.demo.model.Anunt;
import com.example.demo.model.Statistica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StatisticaRepository extends JpaRepository<Statistica, Long> {
    @Query("SELECT u FROM Statistica u WHERE u.user.id = :userId")
    Statistica findByUserId(long userId);



}
