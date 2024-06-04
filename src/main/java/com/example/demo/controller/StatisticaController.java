package com.example.demo.controller;

import com.example.demo.model.Statistica;
import com.example.demo.service.StatisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statistica")
public class StatisticaController {
    @Autowired
    private StatisticaService statisticaService;
    @GetMapping("/user/{userId}")
    @CrossOrigin(origins = "http://localhost:63342")
    public Statistica getStatisticaByUserId (@PathVariable final long userId) {
        return statisticaService.getByUserId(userId);
    }



}
