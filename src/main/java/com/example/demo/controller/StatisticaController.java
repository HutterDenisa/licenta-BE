package com.example.demo.controller;

import com.example.demo.model.Statistica;
import com.example.demo.service.StatisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statistica")
public class StatisticaController {
    @Autowired
    private StatisticaService statisticaService;
    @CrossOrigin
    @GetMapping("/user/{userId}")
    public ResponseEntity<Statistica> getStatisticaByUserId(@PathVariable final long userId) {
        Statistica statistica = statisticaService.getByUserId(userId);
        if (statistica != null) {
            return ResponseEntity.ok(statistica);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }



}