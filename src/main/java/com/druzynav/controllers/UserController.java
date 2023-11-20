package com.druzynav.controllers;

import com.druzynav.auth.JwtService;
import com.druzynav.auth.dto.TokenRequest;
import com.druzynav.models.characteristic.dto.CharacteristicsDTO;
import com.druzynav.models.flat.Flat;
import com.druzynav.models.flat.FlatPhoto;
import com.druzynav.models.photo.Photo;
import com.druzynav.models.user.Role;
import com.druzynav.models.user.Status;
import com.druzynav.models.user.User;
import com.druzynav.models.user.dto.UserDTO;
import com.druzynav.models.user.dto.UserSpecificDTO;
import com.druzynav.repositories.*;
import com.druzynav.services.UserCharacteristicService;
import com.druzynav.services.UserService;
import com.druzynav.services.UserSpecificService;
import com.github.javafaker.Faker;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Random;

@RestController
@AllArgsConstructor
@CrossOrigin
public class UserController {


    private JwtService jwtService;

    private UserService userService;
    private UserRepository userRepository;

    private CharacteristicsRepository characteristicsRepository;
    private UserCharacteristicService userCharacteristicService;

    private UserSpecificService userSpecificService;

    private FlatRepository flatRepository;
    private PhotoRepository photoRepository;

    private FlatPhotosRepository flatPhotosRepository;

    @PostMapping(value = "/api/v1/userInfo")
    public ResponseEntity test(@RequestBody TokenRequest request,
                                               HttpServletResponse response,
                                                HttpServletRequest req) {
        if(jwtService.isTokenExpired(request.getToken())){
            return ResponseEntity.ok("Expired");
        }
        UserDTO userResponse = userService.UserInfo(request);

        System.out.println(req.getHeader("Authorization"));


        if(userResponse.getUserId() == -1){
            return ResponseEntity.ok("User not found");
        }

        return ResponseEntity.ok(userResponse);

    }

    @PostMapping(value = "/api/v1/saveUserInfo")
    public ResponseEntity saveUserInfo(@RequestBody UserDTO request){
        return userService.SaveUserInfo(request);
    }

    @PostMapping(value = "/api/v1/activity")
    public ResponseEntity<String> updateActivity(@RequestBody String email){
        return userService.updateActivity(email);
    }


    @GetMapping(value = "/api/v1/user/{id}")
    public ResponseEntity<UserSpecificDTO> getUser(@PathVariable Integer id){
        return userSpecificService.userSpecific(id);
    }


    @PostMapping(value = "/populate")
    public void populateWithMockUsers() throws IOException {
        Faker faker = new Faker();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwd = encoder.encode("qwerty");
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            User user = User.builder()
                    .firstname(faker.name().firstName())
                    .lastname(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .phonenumber(faker.phoneNumber().cellPhone())
                    .password(passwd)
                    .gender(randomGender())
                    .role(Role.USER)
                    .age(faker.number().numberBetween(18, 100))
                    .description("Mam tutaj jakis opis!")
                    .enabled(true)
                    .status(Status.INACTIVE)
                    .still_looking(true)
                    .build();
            userRepository.save(user);

            Photo photo = Photo.builder()
                    .user(user)
                    .name(faker.funnyName().toString() + ".jpg")
                    .data(getImageBytes(sendGetRequest("https://api.unsplash.com/photos/random?client_id=" + System.getenv("IMAGEAPIKEY"))))
                    .build();
            photoRepository.save(photo);

            CharacteristicsDTO characteristicsDTO = CharacteristicsDTO.builder()
                    .sleepTime(randomCharacteristics())
                    .cooking(randomCharacteristics())
                    .invitingFriends(randomCharacteristics())
                    .timeSpentOutsideHome(randomCharacteristics())
                    .characterType(randomCharacteristics())
                    .talkativity(randomCharacteristics())
                    .conciliatory(randomCharacteristics())
                    .likesPets(random.nextInt(1))
                    .hasPets(random.nextInt(1))
                    .smokes(random.nextInt(1))
                    .drinks(random.nextInt(1))
                    .isStudent(random.nextInt(1))
                    .works(random.nextInt(1))
                    .acceptsPets(random.nextInt(1))
                    .acceptsSmoking(random.nextInt(1))
                    .preferedGender(randomGender())
                    .livesIn(randomCity())
                    .userId(i+1)
                    .build();
            userCharacteristicService.saveUserChar(characteristicsDTO);

            Flat flat = Flat.builder()
                    .description("Mam tutaj jakis opis!")
                    // Double.valueOf(faker.address().latitude())
                    .latitude(2.5555)
                    //Double.valueOf(faker.address().longitude())
                    .longitude(1.4444)
                    .user(user)
                    .people_count(faker.number().numberBetween(1, 5))
                    .room_count(faker.number().numberBetween(0, 4))
                    .search_option(faker.number().numberBetween(0, 4))
                    .build();
            flatRepository.save(flat);

            FlatPhoto flatPhoto = FlatPhoto.builder()
                    .flat(flat)
                    .file_name(faker.funnyName().toString() + ".jpg")
                    .data(getImageBytes(sendGetRequest("https://api.unsplash.com/photos/random?client_id=" + System.getenv("IMAGEAPIKEY"))))
                    .build();
            flatPhotosRepository.save(flatPhoto);
        }
    }

    private String randomGender(){
        Random random = new Random();

        String male = "M";
        String female = "K";
        String non = "O";

        int number = random.nextInt(3);
        switch (number) {
            case 0:
                return male;
            case 1:
                return female;
            case 2:
                return non;
            default:
                return non;
        }
    }

    private Integer randomCharacteristics(){
        Random random = new Random();
        //Losowanie liczb z akresu od 1 do 5
        Integer number = random.nextInt(5) + 1;
        return number;
    }

    private String randomCity(){
        Random random = new Random();
        // Losowanie miasta z listy
        String city1 = "Toruń";
        String city2 = "Warszawa";
        String city3 = "Poznań";

        int number = random.nextInt(3);
        switch (number) {
            case 0:
                return city1;
            case 1:
                return city2;
            case 2:
                return city3;
            default:
                return city3;
        }
    }

    private static byte[] getImageBytes(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream inputStream = url.openStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        }
    }

    public static String sendGetRequest(String apiUrl) throws IOException {
        StringBuilder response = new StringBuilder();

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        String imageUrl = null;

        try {
            JsonElement jsonElement = JsonParser.parseString(response.toString());
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            imageUrl = jsonObject.getAsJsonObject("urls").get("small_s3").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageUrl;
    }

    public static String extractImageUrl(String jsonResponse) {
        String imageUrl = null;

        try {
            JsonElement jsonElement = JsonParser.parseString(jsonResponse);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            imageUrl = jsonObject.getAsJsonObject("urls").get("small_s3").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageUrl;
    }
}

