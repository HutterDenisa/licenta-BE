package com.example.demo.controller;

import com.example.demo.model.Eveniment;
import com.example.demo.model.Statistica;
import com.example.demo.model.StatisticaEveniment;
import com.example.demo.service.StatisticaEvenimentService;
import com.example.demo.service.StatisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statisticaeveniment")
public class StatisticaEvenimentController {
    @Autowired
    private StatisticaEvenimentService statisticaevenimentService;
    @GetMapping("/user/{userId}")
    @CrossOrigin(origins = "http://localhost:63342")
    public StatisticaEveniment getEvenimentByUserId (@PathVariable final long userId) {
        return statisticaevenimentService.getByUserId(userId);
    }



}
