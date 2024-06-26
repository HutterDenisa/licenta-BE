package com.example.demo.service;

import com.example.demo.exceptions.NoUserFoundByIdException;
import com.example.demo.exceptions.NoUserFoundException;
import com.example.demo.model.Anunt;
import com.example.demo.model.Statistica;
import com.example.demo.model.User;
import com.example.demo.repository.StatisticaRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatisticaService statisticaService;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public List<User> findByNameContaining(String name) {
        return userRepository.findByNameContaining(name);
    }

    public User getUserById (final Long id) {
        Optional<User> userOptional =  userRepository.findById(id);
        return userOptional.orElseThrow(() -> new NoUserFoundException(HttpStatus.NOT_FOUND));
    }

    public boolean deleteUserById (Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        else
            throw(new NoUserFoundByIdException(HttpStatus.NOT_FOUND));
    }

    public User saveUser(final User user) {
        User result = userRepository.save(user);
        statisticaService.saveStatistica(user);
        return result;
    }

    public User getUserByEmail(String email) {
        Optional<User> userOptional =  userRepository.findByEmail(email);
        return userOptional.orElseThrow(() -> new NoUserFoundException(HttpStatus.NOT_FOUND));
    }
}
