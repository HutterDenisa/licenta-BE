package com.example.demo.controller;

import com.example.demo.model.Mesaj;
import com.example.demo.service.MesajService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
@RequestMapping("/mesaj")
public class MesajController {

    @Autowired
    private MesajService mesajService;

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping
    public List<Mesaj>getAllMesaje(){
        return mesajService.getAllMesaje();
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping
    public Mesaj createMesaj(@RequestParam("userId") Long userId, @RequestParam("mesaj") String mesaj) {
        return mesajService.saveMesaj(userId, mesaj);
    }
}
