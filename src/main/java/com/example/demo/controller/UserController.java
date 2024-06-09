package com.example.demo.controller;

import com.example.demo.model.Anunt;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public User getUserById (@PathVariable final long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User loginUser) {
        // Verifică credențialele utilizatorului
        User existingUser = userService.getUserByEmail(loginUser.getEmail());
        System.out.println("login");
        if (existingUser != null && existingUser.getPassword().equals(loginUser.getPassword())) {
            // Autentificare reușită
            System.out.println("in if");
            String token = generateToken(existingUser.getId());

            // Returnează un obiect JSON care conține token și userId
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", existingUser.getId());

            System.out.println("Conectare reușită! User ID: " + existingUser.getId());
            return ResponseEntity.ok(response);
        } else {
            // Autentificare eșuată
            System.out.println("in else");
            System.out.println("Eșec la conectare. User: " + loginUser.getEmail() + ", Password: " + loginUser.getPassword());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Email sau parolă incorecte."));        }
    }

    private String generateToken(long userId) {
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 864000000)) // Expiră în 10 zile
                .signWith(secretKey)
                .compact();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteUserById (@PathVariable final long id) {
        boolean deleted = userService.deleteUserById(id);
        if (deleted)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }


    @PostMapping
    public User createUser(@RequestBody final User user) {
        return userService.saveUser(user);
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/search/{name}")
    public ResponseEntity<List<User>> searchUserByName(@PathVariable String name) {
        List<User> results = userService.findByNameContaining(name);
        if(results.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

}
