package com.example.demo.service;

import com.example.demo.exceptions.NoAnuntFoundByIdException;
import com.example.demo.model.Anunt;
import com.example.demo.repository.AnuntRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnuntService {
    @Autowired
    private AnuntRepository anuntRepository;


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public List<Anunt> getAllAnunturi() {
        return anuntRepository.findAll();
    }

    public List<Anunt> getAnuntByName(String name) {
        return anuntRepository.findByName(name);
    }

    public List<Anunt> getByRoata(String oras, String gen, String tip, String culoare) {
        return anuntRepository.findByOrasAndGenAndTipAndCuloare(oras, gen, tip, culoare);
    }

    public List<Anunt> getByTipAnunt(String tip) {
        return anuntRepository.findByTipAnunt(tip);
    }

    public boolean deleteAnuntById(long id) {
        if (anuntRepository.existsById(id)) {
            anuntRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Anunt toggleLike(Long id) {
        Anunt anunt = anuntRepository.findById(id)
                .orElseThrow(() -> new NoAnuntFoundByIdException(HttpStatus.NOT_FOUND));

        int currentLikes = anunt.getNrLikes();
        anunt.setNrLikes(currentLikes + 1); // Incrementă în mod implicit

        // Verifică dacă utilizatorul a dat deja like și decide dacă trebuie să decrementeze
        if (anunt.isLikedByCurrentUser()) {
            anunt.setNrLikes(currentLikes - 1);
        }

        // Actualizează starea like-ului pentru utilizatorul curent
        anunt.setLikedByCurrentUser(!anunt.isLikedByCurrentUser());

        return anuntRepository.save(anunt);
    }

    public List<Anunt> getAnunturiByUserId(Long userId) {
        return anuntRepository.findByUserId(userId);
    }

    public List<Anunt> findByNameContaining(String name) {
        return anuntRepository.findByNameContaining(name);
    }

    public Anunt saveAnunt(Anunt anunt) {
        this.checkAndNotifyForSimilarAnunt(anunt);
        return anuntRepository.save(anunt);
    }

    public Anunt getAnuntById(Long id) throws NoAnuntFoundByIdException {
        return anuntRepository.findById(id)
                .orElseThrow(() -> new NoAnuntFoundByIdException(HttpStatus.NOT_FOUND));
    }

    public List<Anunt> findSimilarAnunturi(String tipAnunt, String oras, String tip, String culoare, String gen, String rasa) {
        // Exemplu simplificat pentru a evalua similitudinea totală și parțială
        List<Anunt> allAnunturi = anuntRepository.findByTipAnuntAndOrasAndTipAndCuloareAndGenAndRasa(tipAnunt, oras, tip, culoare, gen, rasa);
        return allAnunturi.stream().filter(anunt -> {
            int matchScore = 0;
            if (anunt.getOras().equalsIgnoreCase(oras)) matchScore++;
            if (anunt.getTip().equalsIgnoreCase(tip)) matchScore++;
            if (anunt.getCuloare().equalsIgnoreCase(culoare)) matchScore++;
            if (anunt.getGen().equalsIgnoreCase(gen)) matchScore++;
            if (anunt.getRasa().equalsIgnoreCase(rasa)) matchScore++;
            // Definim similitudinea totală ca având toate criteriile identice
            return matchScore == 5;
        }).collect(Collectors.toList());
    }
    public void checkAndNotifyForSimilarAnunt(Anunt anunt) {
        String tipAnuntOpposite = anunt.getTipAnunt().equals("pierdut") ? "gasit" : "pierdut";
        List<Anunt> similarAnunturi = anuntRepository.findByTipAnuntAndOrasAndTipAndCuloareAndGenAndRasa(
                tipAnuntOpposite, anunt.getOras(), anunt.getTip(), anunt.getCuloare(), anunt.getGen(), anunt.getRasa());

        if (!similarAnunturi.isEmpty()) {
            for (Anunt similar : similarAnunturi) {
                String message = "S-a găsit un anunt similar cu titlul '" + similar.getName() +
                        "' postat de utilizatorul '" + similar.getUser().getUsername() + "'.";
                messagingTemplate.convertAndSend("/topic/notifications", message);
            }
        }
    }
}
