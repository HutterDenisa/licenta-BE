package com.example.demo.service;

import com.example.demo.exceptions.NoAnuntFoundByIdException;
import com.example.demo.model.Anunt;
import com.example.demo.model.Mesaj;
import com.example.demo.model.User;
import com.example.demo.repository.AnuntRepository;
import com.example.demo.repository.MesajRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MesajService {
    @Autowired
    private MesajRepository mesajRepository;

    @Autowired
    private UserService userService;


    public Mesaj saveMesaj(Long userId, String mesaj) {
        User user = userService.getUserById(userId);
        LocalDateTime timestamp = LocalDateTime.now();
        Mesaj newMesaj = new Mesaj(user, mesaj, timestamp);
        return mesajRepository.save(newMesaj);
    }



    public List<Mesaj> getAllMesaje() {
        return mesajRepository.findAll();
    }

}
