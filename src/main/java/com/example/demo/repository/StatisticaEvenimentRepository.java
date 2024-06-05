package com.example.demo.repository;


import com.example.demo.model.StatisticaEveniment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StatisticaEvenimentRepository extends JpaRepository<StatisticaEveniment, Long> {
    @Query("SELECT u FROM Eveniment u WHERE u.user.id = :userId")
    StatisticaEveniment findByUserId(long userId);


}
