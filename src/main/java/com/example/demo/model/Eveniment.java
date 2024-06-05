package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name = "eveniment", schema = "public")
public class Eveniment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;
        private String name;
        private String oras;
        private String locatie;
        private String description;

        @ManyToOne
        @JoinColumn(name = "userId", referencedColumnName = "id")
        private User user;
        private String imagePath1;
        private int nrLikes;
        private boolean likedByCurrentUser;


    }


