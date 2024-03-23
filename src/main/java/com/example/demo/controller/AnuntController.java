package com.example.demo.controller;

import com.example.demo.exceptions.NoAnuntFoundByIdException;
import com.example.demo.model.Anunt;
import com.example.demo.model.TipAnunt;
import com.example.demo.model.User;
import com.example.demo.service.AnuntService;
import com.example.demo.service.UserService;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@RequestMapping("/anunt")
public class AnuntController {
    @Autowired
    private AnuntService anuntService;
    @Value("${images.upload.directory}")
    private String uploadDirectory;
    @Autowired
    private UserService userService;

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
    @GetMapping("/tip/{tip}")
    public List<Anunt> getAnuntByTipAnunt (@PathVariable String tip) {
        return anuntService.getByTipAnunt(TipAnunt.valueOf(tip));
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
                .contentType(MediaType.IMAGE_JPEG) // sau MediaType.IMAGE_PNG, în funcție de tipul imaginilor
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Anunt> createAnunt(

            @RequestParam("userId") Long userId,
            @RequestParam("title") String title,
            @RequestPart("tip") String tip,
            @RequestPart("rasa") String rasa,
            @RequestPart("gen") String gen,
            @RequestPart("culoare") String culoare,
            @RequestPart("varsta") int varsta,
            @RequestPart("oras") String oras,
            @RequestPart("adresa") String adresa,
            @RequestParam("description") String description,
            @RequestPart("image1") MultipartFile image1,
            @RequestPart("image2") MultipartFile image2,
            @RequestPart("image3") MultipartFile image3,
            @RequestPart("image4") MultipartFile image4,
            @RequestPart("image5") MultipartFile image5,
            @RequestPart("tipAnunt") String tipAnunt,
            @RequestParam("nrLikes") int nrLikes,
            @RequestParam("likedByCurrentUser") boolean likedByCurrentUser) {

        try {
            Anunt anunt = new Anunt();
            anunt.setName(title);
            anunt.setDescription(description);

            // Salvează imaginea și actualizează calea în obiectul Anunt
            String imagePath1 = saveImage(image1);
            anunt.setImagePath1(imagePath1);
            String imagePath2 = saveImage(image2);
            anunt.setImagePath2(imagePath2);
            String imagePath3 = saveImage(image3);
            anunt.setImagePath3(imagePath3);
            String imagePath4 = saveImage(image4);
            anunt.setImagePath4(imagePath4);
            String imagePath5 = saveImage(image5);
            anunt.setImagePath5(imagePath5);

            anunt.setAdresa(adresa);
            anunt.setTipAnunt(String.valueOf(tipAnunt));
            anunt.setCuloare(culoare);
            anunt.setGen(gen);
            anunt.setOras(oras);
            anunt.setRasa(rasa);
            anunt.setVarsta(varsta);
            anunt.setTip(tip);

            User user = userService.getUserById(userId);
            anunt.setNrLikes(nrLikes);
            anunt.setLikedByCurrentUser(likedByCurrentUser);
            anunt.setUser(user);

            // Salvează obiectul Anunt în baza de date
            Anunt savedAnunt = anuntService.saveAnunt(anunt);

            return ResponseEntity.ok()
                    .header("Access-Control-Allow-Origin", "http://localhost:63342")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(savedAnunt);
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

}
