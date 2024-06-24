package com.example.demo.service;

import com.example.demo.exceptions.NoAnuntFoundByIdException;
import com.example.demo.model.Anunt;
import com.example.demo.model.Eveniment;
import com.example.demo.model.Statistica;
import com.example.demo.repository.AnuntRepository;
import com.example.demo.repository.EvenimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvenimentService {
    @Autowired
    private EvenimentRepository evenimentRepository;

    public List<Eveniment> getAllEvenimente() {
        return evenimentRepository.findAll();
    }

    public List<Eveniment> getEvenimentByName(String name) {
        return evenimentRepository.findByNameContaining(name);
    }

    public boolean deleteEvenimentById(long id) {
        if (evenimentRepository.existsById(id)) {
            evenimentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Eveniment toggleLike(Long id) {
        Eveniment eveniment = evenimentRepository.findById(id)
                .orElseThrow(() -> new NoAnuntFoundByIdException(HttpStatus.NOT_FOUND));

        int currentLikes = eveniment.getNrLikes();
        eveniment.setNrLikes(currentLikes + 1); // Incrementă în mod implicit


        if (eveniment.isLikedByCurrentUser()) {
            eveniment.setNrLikes(currentLikes - 1);
        }


        eveniment.setLikedByCurrentUser(!eveniment.isLikedByCurrentUser());

        Eveniment result = evenimentRepository.save(eveniment);


        return result;
    }

    public List<Eveniment> getEvenimenteByUserId(Long userId) {
        return evenimentRepository.findByUserId(userId);
    }

    public List<Eveniment> findByNameContaining(String name) {
        return evenimentRepository.findByNameContaining(name);
    }

    public Eveniment saveEveniment(Eveniment eveniment) {

        return evenimentRepository.save(eveniment);
    }

    public Eveniment getEvenimentById(Long id) throws NoAnuntFoundByIdException {
        return evenimentRepository.findById(id)
                .orElseThrow(() -> new NoAnuntFoundByIdException(HttpStatus.NOT_FOUND));
    }
}