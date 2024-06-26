package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "anunt", schema = "public")
public class Anunt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;
    private String imagePath1;
    private String tip;
    private String rasa;
    private String gen;
    private String culoare;
    private int varsta;
    private String oras;
    private String adresa;
    private int nrLikes;
    private boolean likedByCurrentUser;
    private String tipAnunt;

}
