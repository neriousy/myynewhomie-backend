package com.druzynav.controllers;

import com.druzynav.auth.JwtService;
import com.druzynav.models.flat.Flat;
import com.druzynav.models.flat.FlatPhoto;
import com.druzynav.models.flat.dto.FlatDTO;
import com.druzynav.models.user.Status;
import com.druzynav.models.user.User;
import com.druzynav.repositories.FlatPhotosRepository;
import com.druzynav.repositories.FlatRepository;
import com.druzynav.repositories.UserRepository;
import com.druzynav.services.FlatService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RestController
public class FlatController {
    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private FlatPhotosRepository flatPhotosRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private FlatService flatService;

    // Tutaj uzytkownik podaje tylko dane opisowe mieszkania, a nie zdjecia
//    @PostMapping(value = "/flat/add")
    ResponseEntity<?> addFlat(HttpServletRequest request, @RequestBody FlatDTO flatDTO) {
        Flat flat = new Flat();
        if (flatDTO == null) {
            System.out.println("Pusto");
            return ResponseEntity.badRequest().build();
        } else {
            System.out.println("Cos jest");
            //-------Na produkcje------------------------
            String authHeader = request.getHeader("Authorization");
            String jwt = authHeader.substring(7);
            String username = jwtService.extractUsername(jwt);

            if (username == null) {
                return ResponseEntity.badRequest().build();
            }
            User user = userRepository.findByEmail(username).get();
            //--------------------------------------------
            //-------Do testow------------------------
            //User user = userRepository.findById(2).get();
            //----------------------------------------
            user.setStatus(Status.ACTIVE);
            userRepository.save(user);

            flat.setUser(user);
            flat.setDescription(flatDTO.getDescription());
            flat.setLatitude(flatDTO.getLatitude());
            flat.setLongitude(flatDTO.getLongitude());
            flat.setRoom_count(flatDTO.getNumberOfRooms());
            flat.setPeople_count(flatDTO.getNumberOfPeople());
            flat.setSearch_option(flatDTO.getSearchOption());

            flatRepository.save(flat);
        }

        return ResponseEntity.ok(flat.getId());
    }

    @PostMapping(value = "/flat/add-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> addPhoto(
            HttpServletRequest request,
            @RequestParam("files") List<MultipartFile> files
    ) throws IOException {

        // Sprawdzanie czy nie ma duplikatow zdjec
        // TODO: napisac to sprawdzenie§

        String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String username = jwtService.extractUsername(jwt);
        if (username == null) {
            return ResponseEntity.badRequest().build();
        }

        User user = userRepository.findByEmail(username).get();
//        User user = userRepository.findById(1).get();

        Optional<Flat> optionalFlat = Optional.ofNullable(flatRepository.findByUserId(user.getId()));
        System.out.println(optionalFlat);
        if (optionalFlat.isEmpty()) {
            throw new RuntimeException("Mieszkanie o podanym ID nie istnieje.");
        }

        Flat flat = optionalFlat.get();
        System.out.println(flat.getId());

        for (MultipartFile file : files) {
            byte[] data = file.getBytes();
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

            FlatPhoto flatPhoto = FlatPhoto.builder()
                    .flat(flat)
                    .file_name(fileName)
                    .data(data)
                    .build();

            flatPhotosRepository.save(flatPhoto);
        }

        return ResponseEntity.ok("Zapisano zdjęcia");
    }

    @GetMapping(value = "/flat/photos/{id}")
    public ResponseEntity<InputStreamResource> getFlatPhotos(@PathVariable("id") Integer id) throws IOException {
        Optional<Flat> optionalFlat = Optional.ofNullable(flatRepository.findByUserId(id));
        if (optionalFlat.isEmpty()) {
            throw new RuntimeException("Mieszkanie o podanym ID nie istnieje.");
        }

        Flat flat = optionalFlat.get();
        List<FlatPhoto> flatPhotos = flatPhotosRepository.findByFlat(flat);
        if (flatPhotos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<ByteArrayInputStream> inputStreams = new ArrayList<>();
        List<String> filenames = new ArrayList<>();

        // Collect the input streams and filenames of each photo
        for (FlatPhoto flatPhoto : flatPhotos) {
            byte[] photoData = flatPhoto.getData();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(photoData);
            inputStreams.add(inputStream);
            filenames.add(flatPhoto.getFile_name());
        }

        // Create a zip file containing the images
        ByteArrayOutputStream zipOutput = new ByteArrayOutputStream();
        ZipOutputStream zipStream = new ZipOutputStream(zipOutput);
        byte[] buffer = new byte[1024];

        for (int i = 0; i < inputStreams.size(); i++) {
            ByteArrayInputStream inputStream = inputStreams.get(i);
            String filename = filenames.get(i);

            zipStream.putNextEntry(new ZipEntry(filename));

            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                zipStream.write(buffer, 0, len);
            }

            zipStream.closeEntry();
            inputStream.close();
        }

        zipStream.close();
        zipOutput.close();

        // Set the appropriate headers for the zip file
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename("flat_photos.zip").build());

        return new ResponseEntity<>(new InputStreamResource(new ByteArrayInputStream(zipOutput.toByteArray())), headers, HttpStatus.OK);
    }

}
