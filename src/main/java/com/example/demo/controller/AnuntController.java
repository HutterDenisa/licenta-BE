package com.example.demo.controller;


import com.example.demo.exceptions.NoAnuntFoundByIdException;
import com.example.demo.model.Anunt;
import com.example.demo.model.User;
import com.example.demo.service.AnuntService;
import com.example.demo.service.StatisticaService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/anunt")
public class AnuntController {
    @Autowired
    private AnuntService anuntService;

    @Value("${images.upload.directory}")
    private String uploadDirectory;

    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping
    public List<Anunt> getAllAnunturi(){
        return anuntService.getAllAnunturi();
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/anunt/{name}")
    public List<Anunt> getAnuntByName (@PathVariable String name) {
        return anuntService.getAnuntByName(name);
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/roata/{oras}/{gen}/{tip}/{culoare}")
    public List<Anunt> getAnuntByRoata (@PathVariable String oras, @PathVariable String gen, @PathVariable String tip, @PathVariable String culoare) {
        return anuntService.getByRoata(oras,gen,tip,culoare);
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/tip/{tip}")
    public List<Anunt> getAnuntByTipAnunt (@PathVariable String tip) {
        return anuntService.getByTipAnunt(tip);
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteAnuntById (@PathVariable final long id) {
        boolean deleted = anuntService.deleteAnuntById(id);
        if (deleted)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/image/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) throws IOException {
        // Construiește calea completă a imaginii
        String imagePath = uploadDirectory + "/" + imageName;

        // Încarcă imaginea și returnează-o sub formă de răspuns
        Resource imageResource = new FileSystemResource(imagePath);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG) // sau MediaType.IMAGE_PNG, în funcție de tipul imaginilor
                .body(imageResource);
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PutMapping("/like/{id}")
    public ResponseEntity<Anunt> toggleLike(@PathVariable Long id) {
        try {
            Anunt updatedAnunt = anuntService.toggleLike(id);
            return new ResponseEntity<>(updatedAnunt, HttpStatus.OK);
        } catch (NoAnuntFoundByIdException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/userId/{userId}")
    public List<Anunt> getAnuntByUserId (@PathVariable Long userId) {
        return anuntService.getAnunturiByUserId(userId);
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PutMapping("/edit/{id}")
    public ResponseEntity<Anunt> editAnunt(@PathVariable Long id, @RequestBody Anunt updatedAnunt) {
        try {
            Anunt existingAnunt = anuntService.getAnuntById(id);
            if (existingAnunt != null) {
                System.out.println("Solicitarea a ajuns în editAnunt cu id: " + id);
                // Actualizează detaliile anunțului cu noile informații primite în corpul cererii
                existingAnunt.setName(updatedAnunt.getName());
                existingAnunt.setDescription(updatedAnunt.getDescription());
                // Alte câmpuri pentru actualizare

                // Salvează anunțul actualizat în baza de date
                Anunt savedAnunt = anuntService.saveAnunt(existingAnunt);

                //HttpHeaders headers = new HttpHeaders();
                //headers.add("Access-Control-Allow-Origin", "http://127.0.0.1:5500");

                return ResponseEntity.ok()
                        .body(savedAnunt);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NoAnuntFoundByIdException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/old/edit/{id}")
    public ResponseEntity<Anunt> getAnuntForEdit(@PathVariable Long id) {
        try {
            Anunt existingAnunt = anuntService.getAnuntById(id);
            if (existingAnunt != null) {
                return ResponseEntity.ok(existingAnunt);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NoAnuntFoundByIdException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping
    public ResponseEntity<Anunt> createAnunt(
            @RequestParam("userId") Long userId,
            @RequestParam("title") String title,
            @RequestParam("tip") String tip,
            @RequestParam("rasa") String rasa,
            @RequestParam("gen") String gen,
            @RequestParam("culoare") String culoare,
            @RequestParam("varsta") int varsta,
            @RequestParam("oras") String oras,
            @RequestParam("adresa") String adresa,
            @RequestParam("description") String description,
            @RequestParam("image1") MultipartFile image1,
            @RequestParam("tipAnunt") String tipAnunt,
            @RequestParam("nrLikes") int nrLikes,
            @RequestParam("likedByCurrentUser") boolean likedByCurrentUser) {

        try {
            Anunt anunt = new Anunt();
            anunt.setName(title);
            anunt.setDescription(description);
            anunt.setAdresa(adresa);
            anunt.setTipAnunt(tipAnunt);
            anunt.setCuloare(culoare);
            anunt.setGen(gen);
            anunt.setOras(oras);
            anunt.setRasa(rasa);
            anunt.setVarsta(varsta);
            anunt.setTip(tip);

            // Salvează imaginea și actualizează calea în obiectul Anunt
            String imagePath1 = saveImage(image1);
            anunt.setImagePath1(imagePath1);
            User user = userService.getUserById(userId);
            anunt.setNrLikes(nrLikes);
            anunt.setLikedByCurrentUser(likedByCurrentUser);
            anunt.setUser(user);
            System.out.println(anunt);

            // Salvează obiectul Anunt în baza de date
            Anunt savedAnunt = anuntService.saveAnunt(anunt);

            List<Anunt> similarAnunturi = anuntService.findSimilarAnunturi(
                    anunt.getTipAnunt(), anunt.getOras(), anunt.getTip(), anunt.getCuloare(), anunt.getGen(), anunt.getRasa());

            if (!similarAnunturi.isEmpty()) {
                String jsonMessage = similarAnunturi.stream()
                        .map(similar -> String.format("{\"title\":\"%s\", \"username\":\"%s\", \"email\":\"%s\"}", similar.getName(), similar.getUser().getUsername(), similar.getUser().getEmail()))
                        .collect(Collectors.joining(", ", "[", "]"));
                messagingTemplate.convertAndSend("/topic/notifications", jsonMessage);
            }

            return ResponseEntity.ok().body(savedAnunt);
        } catch (IOException e) {
            // Tratează eroarea în mod corespunzător
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            // Dacă există o altă problemă, tratează-o aici
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String saveImage(MultipartFile image) throws IOException {
        // Generează un nume unic pentru imagine
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String imageName = dateFormat.format(new Date()) + "_" + image.getOriginalFilename();

        // Construiește calea completă a imaginii
        String imagePath = uploadDirectory + "/" + imageName;

        // Salvează imaginea pe disc
        Path destination = Paths.get(imagePath);
        Files.copy(image.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return imagePath;
    }

    // In AnuntController.java
    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/search/{name}")
    public ResponseEntity<List<Anunt>> searchAnuntByName(@PathVariable String name) {
        List<Anunt> results = anuntService.findByNameContaining(name);
        if(results.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

}
