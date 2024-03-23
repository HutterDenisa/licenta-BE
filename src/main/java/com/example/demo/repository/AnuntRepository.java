package com.example.demo.repository;

import com.example.demo.model.Anunt;
import com.example.demo.model.TipAnunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnuntRepository extends JpaRepository<Anunt, Long> {
    @Query("SELECT u FROM Anunt u WHERE u.name = :name")
    List<Anunt> findByName(String name);

    @Query("SELECT u FROM Anunt u WHERE u.user.id = :userId")
    List<Anunt> findByUserId(Long userId);

    @Query("SELECT u FROM Anunt u WHERE u.tipAnunt = :tipAnunt")
    List<Anunt> findByTipAnunt(TipAnunt tipAnunt);
}
