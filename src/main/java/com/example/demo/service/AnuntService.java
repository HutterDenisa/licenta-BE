package com.example.demo.service;

import com.example.demo.exceptions.NoAnuntFoundByIdException;
import com.example.demo.model.Anunt;
import com.example.demo.repository.AnuntRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnuntService {
    @Autowired
    private AnuntRepository anuntRepository;

    @Autowired
    private EmailService emailService;

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

    public Anunt toggleLike(Long id) throws NoAnuntFoundByIdException {
        Anunt anunt = anuntRepository.findById(id)
                .orElseThrow(() -> new NoAnuntFoundByIdException(HttpStatus.NOT_FOUND));
        anunt.setNrLikes(anunt.getNrLikes() + 1);
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
        return anuntRepository.findByTipAnuntAndOrasAndTipAndCuloareAndGenAndRasa(
                tipAnunt, oras, tip, culoare, gen, rasa);
    }

    public void checkAndNotifyForSimilarAnunt(Anunt anunt) {
        String tipAnuntOpposite = anunt.getTipAnunt().equals("pierdut") ? "gasit" : "pierdut";
        List<Anunt> similarAnunturi = findSimilarAnunturi(tipAnuntOpposite, anunt.getOras(), anunt.getTip(), anunt.getCuloare(), anunt.getGen(), anunt.getRasa());

        if (!similarAnunturi.isEmpty()) {
            for (Anunt similar : similarAnunturi) {
                // Construct the HTML content
                String htmlContent = "<h1>Similar Announcement Found!</h1>" +
                        "<p>We have found a similar announcement that might interest you: <strong>" + similar.getName() + "</strong></p>";

                try {
                    emailService.sendHtmlEmail(anunt.getUser().getEmail(), "Similar Announcement Found", htmlContent);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
