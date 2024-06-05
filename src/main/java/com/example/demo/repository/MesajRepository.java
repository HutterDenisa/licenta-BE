package com.example.demo.repository;

import com.example.demo.model.Anunt;
import com.example.demo.model.Mesaj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MesajRepository extends JpaRepository<Mesaj, Long> {
    @Query("SELECT u FROM Mesaj u ORDER BY u.t1")
    List<Mesaj> findAll();
}
