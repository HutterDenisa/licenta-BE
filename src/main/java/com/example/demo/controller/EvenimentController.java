package com.example.demo.controller;


import com.example.demo.exceptions.NoAnuntFoundByIdException;
import com.example.demo.model.Eveniment;
import com.example.demo.model.User;
import com.example.demo.service.EvenimentService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/eveniment")
public class EvenimentController {


    @Autowired
    private EvenimentService evenimentService;

    @Value("${images.upload.directory}")
    private String uploadDirectory;

    @Autowired
    private UserService userService;


    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping
    public List<Eveniment> getAllEvenimente(){
        return evenimentService.getAllEvenimente();
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/eveniment/{name}")
    public List<Eveniment> getEvenimentByName (@PathVariable String name) {
        return evenimentService.getEvenimentByName(name);
    }


    @CrossOrigin(origins = "http://localhost:63342")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteEvenimentById (@PathVariable final long id) {
        boolean deleted = evenimentService.deleteEvenimentById(id);
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
    public ResponseEntity<Eveniment> toggleLike(@PathVariable Long id) {
        try {
            Eveniment updatedEveniment = evenimentService.toggleLike(id);
            return new ResponseEntity<>(updatedEveniment, HttpStatus.OK);
        } catch (NoAnuntFoundByIdException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/userId/{userId}")
    public List<Eveniment> getEvenimentByUserId (@PathVariable Long userId) {
        return evenimentService.getEvenimenteByUserId(userId);
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PutMapping("/edit/{id}")
    public ResponseEntity<Eveniment> editEveniment(@PathVariable Long id, @RequestBody Eveniment updatedEveniment) {
        try {
            Eveniment existingEveniment = evenimentService.getEvenimentById(id);
            if (existingEveniment != null) {
                System.out.println("Solicitarea a ajuns în editAnunt cu id: " + id);
                // Actualizează detaliile anunțului cu noile informații primite în corpul cererii
                existingEveniment.setName(updatedEveniment.getName());
                existingEveniment.setDescription(updatedEveniment.getDescription());
                // Alte câmpuri pentru actualizare

                // Salvează anunțul actualizat în baza de date
                Eveniment savedEveniment = evenimentService.saveEveniment(existingEveniment);

                //HttpHeaders headers = new HttpHeaders();
                //headers.add("Access-Control-Allow-Origin", "http://127.0.0.1:5500");

                return ResponseEntity.ok()
                        .body(savedEveniment);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NoAnuntFoundByIdException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/old/edit/{id}")
    public ResponseEntity<Eveniment> getEvenimentForEdit(@PathVariable Long id) {
        try {
            Eveniment existingEveniment = evenimentService.getEvenimentById(id);
            if (existingEveniment != null) {
                return ResponseEntity.ok(existingEveniment);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NoAnuntFoundByIdException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping
    public ResponseEntity<Eveniment> createEveniment(
            @RequestParam("title") String title,
            @RequestParam("locatie") String locatie,
            @RequestParam("description") String description,
            @RequestParam("userId") long userId,
            @RequestParam("image1") MultipartFile image1,
            @RequestParam("nrLikes") int nrLikes,
            @RequestParam("likedByCurrentUser") boolean likedByCurrentUser) {

        try {
            Eveniment eveniment = new Eveniment();
            eveniment.setName(title);
            eveniment.setLocatie(locatie);
            eveniment.setDescription(description);


            // Salvează imaginea și actualizează calea în obiectul Anunt
            String imagePath1 = saveImage(image1);
            eveniment.setImagePath1(imagePath1);
            User user = userService.getUserById(userId);
            eveniment.setNrLikes(nrLikes);
            eveniment.setLikedByCurrentUser(likedByCurrentUser);
            eveniment.setUser(user);
            System.out.println(eveniment);

            // Salvează obiectul Anunt în baza de date
            Eveniment savedEveniment = evenimentService.saveEveniment(eveniment);


            return ResponseEntity.ok().body(savedEveniment);
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
    public ResponseEntity<List<Eveniment>> searchEvenimentByName(@PathVariable String name) {
        List<Eveniment> results = evenimentService.findByNameContaining(name);
        if(results.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }


}
