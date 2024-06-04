package com.example.demo.service;

import com.example.demo.exceptions.NoAnuntFoundByIdException;
import com.example.demo.model.Anunt;
import com.example.demo.model.Contact;
import com.example.demo.model.Statistica;
import com.example.demo.model.User;
import com.example.demo.repository.StatisticaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatisticaService {
    @Autowired
    private StatisticaRepository statisticaRepository;

    public Statistica getByUserId(long userId) {
        return statisticaRepository.findByUserId(userId);
    }

    public Statistica saveStatistica(User user){
        LocalDateTime initialTime = LocalDateTime.now();
        Statistica statistica = new Statistica(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, initialTime, initialTime, initialTime, initialTime, initialTime, initialTime, initialTime, initialTime, initialTime, initialTime, user);
        return statisticaRepository.save(statistica);
    }

    public Statistica updateStatistica(long id, int act){


        Statistica statistica = statisticaRepository.findById(id)
                .orElseThrow(() -> new NoAnuntFoundByIdException(HttpStatus.NOT_FOUND));

        statistica.setAct1(statistica.getAct2());
        statistica.setAct2(statistica.getAct3());
        statistica.setAct3(statistica.getAct4());
        statistica.setAct4(statistica.getAct5());
        statistica.setAct5(statistica.getAct6());
        statistica.setAct6(statistica.getAct7());
        statistica.setAct7(statistica.getAct8());
        statistica.setAct8(statistica.getAct9());
        statistica.setAct9(statistica.getAct10());
        statistica.setAct10(act);

        statistica.setT1(statistica.getT2());
        statistica.setT2(statistica.getT3());
        statistica.setT3(statistica.getT4());
        statistica.setT4(statistica.getT5());
        statistica.setT5(statistica.getT6());
        statistica.setT6(statistica.getT7());
        statistica.setT7(statistica.getT8());
        statistica.setT8(statistica.getT9());
        statistica.setT9(statistica.getT10());
        statistica.setT10(LocalDateTime.now());

        return statisticaRepository.save(statistica);

    }
}
