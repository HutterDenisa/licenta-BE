package com.example.demo.repository;

import com.example.demo.model.Anunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnuntRepository extends JpaRepository<Anunt, Long> {
    @Query("SELECT u FROM Anunt u WHERE u.oras = :oras AND u.gen = :gen AND u.tip = :tip AND u.culoare = :culoare")
    List<Anunt> findByOrasAndGenAndTipAndCuloare(String oras, String gen, String tip, String culoare);

    @Query("SELECT u FROM Anunt u WHERE u.tipAnunt = :tipAnunt AND u.oras = :oras AND u.tip = :tip AND u.culoare = :culoare AND u.gen = :gen AND u.rasa = :rasa ")
    List<Anunt> findByTipAnuntAndOrasAndTipAndCuloareAndGenAndRasa(
            String tipAnunt, String oras, String tip, String culoare, String gen, String rasa);

    @Query("SELECT u FROM Anunt u WHERE u.name = :name")
    List<Anunt> findByName(String name);

    @Query("SELECT u FROM Anunt u WHERE u.user.id = :userId")
    List<Anunt> findByUserId(Long userId);

    @Query("SELECT u FROM Anunt u WHERE u.tipAnunt = :tipAnunt")
    List<Anunt> findByTipAnunt(String tipAnunt);


    @Query("SELECT u FROM Anunt u WHERE lower(u.name) LIKE lower(concat('%', :name, '%'))")
    List<Anunt> findByNameContaining(String name);

}
