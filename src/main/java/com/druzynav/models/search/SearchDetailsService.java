package com.druzynav.models.search;

import com.druzynav.auth.JwtService;
import com.druzynav.exceptions.SimpleEntryComparator;
import com.druzynav.models.activity.Activity;
import com.druzynav.models.photo.Photo;
import com.druzynav.models.user.User;
import com.druzynav.models.user.dto.SearchCriteriaDTO;
import com.druzynav.models.user.dto.SearchCriteriaExtendedDTO;
import com.druzynav.models.user.dto.SearchDTO;
import com.druzynav.models.userCharacteristic.UserCharacteristic;
import com.druzynav.repositories.ActivityRepository;
import com.druzynav.repositories.PhotoRepository;
import com.druzynav.repositories.UserCharacteristicsRepository;
import com.druzynav.repositories.UserRepository;
import com.druzynav.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class SearchDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private UserCharacteristicsRepository userCharacteristicsRepository;

    public ResponseEntity<List<SearchDTO>> searchSpecificUsers(SearchCriteriaDTO searchCriteriaDTO, HttpServletRequest request) {

        UserSpecyfication userSpecyfication = new UserSpecyfication();

        //Get only active users, who are still looking for a flatmate
        userSpecyfication.addStillLookingSpecification();

        //User specifies only age_from and age_to
        if (searchCriteriaDTO.age_from > 0 && searchCriteriaDTO.age_to > 0 && searchCriteriaDTO.age_to >= searchCriteriaDTO.age_from) {
            userSpecyfication.addAgeBetweenSpecification(searchCriteriaDTO.age_from, searchCriteriaDTO.age_to);
        } else {
            //In case the user specifies only age_from or age_to
            if (searchCriteriaDTO.age_from > 0) {
                userSpecyfication.addAgeGreaterSpecification(searchCriteriaDTO.age_from);
            } else if (searchCriteriaDTO.age_to > 0) {
                userSpecyfication.addAgeLessSpecification(searchCriteriaDTO.age_to);
            }
        }

        //If user choose 'other', show both genders
        if ((searchCriteriaDTO.gender).equals("M") || (searchCriteriaDTO.gender).equals("K")) {
            userSpecyfication.addGenderSpecification(searchCriteriaDTO.gender);
        }

        //If 'city' is empty, show people from every city from database
        if (searchCriteriaDTO.city != null) {
            userSpecyfication.addCitySpecification(searchCriteriaDTO.city);
        }

        //Looking for users with status:

        //Is this user a student?
        if (searchCriteriaDTO.ifStudent) {
            userSpecyfication.addStudentStatusSpecification();
        }

        //Does this person work?
        if (searchCriteriaDTO.ifWorking) {
            userSpecyfication.addWorkingStatusSpecification();
        }

        //Does this person smoke?
        if (searchCriteriaDTO.ifSmoking) {
            userSpecyfication.addSmokingStatusSpecification();
        }
        //Does this person drink alcohol?

        if (searchCriteriaDTO.ifDrinkingAlc) {
            userSpecyfication.addDrinkingAlcStatusSpecification();
        }

        List<User> userList = userRepository.findAll(userSpecyfication);
        //Wyciagamy maila obecnie zlogowanego uzytkownika
        //-------Kod na produkcje-------
        //Moze wystepowac problem z jwt
        String token = request.getHeader("Authorization");
        String currentUserMail = jwtService.extractUsername(token.substring(7));
        //Usuwa uzytkownika o zadanym adresie mail z listy uzytkownikow podanych jako wynik wyszukiwania

        //Optional czy nie jest pusty
        User currentUser = userRepository.findByEmail(currentUserMail).get();

        userList.remove(currentUser);


        List<SearchDTO> searchDTOS = new ArrayList<>();
        SearchDTO searchDTO = null;
        Optional<Activity> activity = null;
        Boolean online = false;
        Optional<Photo> optionalPhoto = null;
        byte[] photo =  null;

        for (User user : userList) {
            activity = activityRepository.findByEmail(user.getEmail());
            if (activity.isPresent()) {
                online = userService.isActive(activity.get());
            } else {
                System.err.println("There is no such thing as activity");
            }
            optionalPhoto = photoRepository.findByUserId(user.getId());
            if(optionalPhoto.isPresent()){
                photo = optionalPhoto.get().getData();
            }else{
                photo = null;
            }


            searchDTO = new SearchDTO(user.getId(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getAge(),
                    user.getGender(),
                    online,
                    photo,
                    -1.0,
                    user.getDescription());
            searchDTOS.add(searchDTO);
        }

        return new ResponseEntity<>(searchDTOS, HttpStatus.OK);
    }

    // Filtorwanie szczególowe
    public ResponseEntity<List<SearchDTO>> searchSpecificUsersWithFilters(SearchCriteriaExtendedDTO searchCriteriaDTO, HttpServletRequest request) {

        UserSpecyfication userSpecyfication = new UserSpecyfication();

        //Get only active users, who are still looking for a flatmate
        userSpecyfication.addStillLookingSpecification();

        //User specifies only age_from and age_to
        if (searchCriteriaDTO.age_from > 0 && searchCriteriaDTO.age_to > 0 && searchCriteriaDTO.age_to >= searchCriteriaDTO.age_from) {
            userSpecyfication.addAgeBetweenSpecification(searchCriteriaDTO.age_from, searchCriteriaDTO.age_to);
        } else {
            //In case the user specifies only age_from or age_to
            if (searchCriteriaDTO.age_from > 0) {
                userSpecyfication.addAgeGreaterSpecification(searchCriteriaDTO.age_from);
            } else if (searchCriteriaDTO.age_to > 0) {
                userSpecyfication.addAgeLessSpecification(searchCriteriaDTO.age_to);
            }
        }

        //If user choose 'other', show both genders
        if ((searchCriteriaDTO.gender).equals("M") || (searchCriteriaDTO.gender).equals("K")) {
            userSpecyfication.addGenderSpecification(searchCriteriaDTO.gender);
        }

        //If 'city' is empty, show people from every city from database
        if (searchCriteriaDTO.city != null) {
            userSpecyfication.addCitySpecification(searchCriteriaDTO.city);
        }

        //Looking for users with status:

        //Is this user a student?
        if (searchCriteriaDTO.ifStudent) {
            userSpecyfication.addStudentStatusSpecification();
        }

        //Does this person work?
        if (searchCriteriaDTO.ifWorking) {
            userSpecyfication.addWorkingStatusSpecification();
        }

        //Does this person smoke?
        if (searchCriteriaDTO.ifSmoking) {
            userSpecyfication.addSmokingStatusSpecification();
        }
        //Does this person drink alcohol?

        if (searchCriteriaDTO.ifDrinkingAlc) {
            userSpecyfication.addDrinkingAlcStatusSpecification();
        }

        //Dodanie elementow dotyczących mieszkania
        userSpecyfication.addFlatSpecification(searchCriteriaDTO);


        List<User> userList = userRepository.findAll(userSpecyfication);
        System.out.println(userList.size());
        //Wyciagamy maila obecnie zlogowanego uzytkownika
        //-------Kod na produkcje-------
        //Moze wystepowac problem z jwt
        String token = request.getHeader("Authorization");
        String currentUserMail = jwtService.extractUsername(token.substring(7));
        //Usuwa uzytkownika o zadanym adresie mail z listy uzytkownikow podanych jako wynik wyszukiwania

        //Optional czy nie jest pusty
        User currentUser = userRepository.findByEmail(currentUserMail).get();

        userList.remove(currentUser);

        //-----test-----
//        User currentUser = userRepository.findById(1).get();


        List<SearchDTO> searchDTOS = new ArrayList<>();
        SearchDTO searchDTO = null;
        Optional<Activity> activity = null;
        Boolean online = false;
        Optional<Photo> optionalPhoto = null;
        byte[] photo =  null;

        for (User user : userList) {
            activity = activityRepository.findByEmail(user.getEmail());
            if (activity.isPresent()) {
                online = userService.isActive(activity.get());
            } else {
                System.err.println("There is no such thing as activity");
            }
            optionalPhoto = photoRepository.findByUserId(user.getId());
            if(optionalPhoto.isPresent()){
                photo = optionalPhoto.get().getData();
            }else{
                photo = null;
            }


            searchDTO = new SearchDTO(user.getId(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getAge(),
                    user.getGender(),
                    online,
                    photo,
                    -1.0,
                    user.getDescription());
            searchDTOS.add(searchDTO);
        }

        return new ResponseEntity<>(searchDTOS, HttpStatus.OK);
    }

    public ResponseEntity<List<SearchDTO>> searchSimilarUser(HttpServletRequest request) {

        /**
         * 1. Znajduje wszytskich uzytkownikow
         * 2. Usuwam z listy obecnie zalogowanego uzytkownika
         * 3. Dla kazdego uzytkownika z listy obliczam score
         * 4. Sortuje liste po score
         * 5. Zwracam liste uzytkownikow posortowana po score
         * */

        UserSpecyfication userSpecyfication = new UserSpecyfication();
        //Wyciagamy maila obecnie zlogowanego uzytkownika
        String token = request.getHeader("Authorization");
        String currentUserMail = jwtService.extractUsername(token.substring(7));
//        //Usuwa uzytkownika o zadanym adresie mail z listy uzytkownikow podanych jako wynik wyszukiwania
        Optional<User> optionalUser = userRepository.findByEmail(currentUserMail);
//        Optional<User> optionalUser = userRepository.findById(1);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User currentUser = optionalUser.get();

        List<UserCharacteristic> userCharacteristics = userCharacteristicsRepository.findByUserId(currentUser.getId());
        Collections.sort(userCharacteristics, Comparator.comparingInt(UserCharacteristic::getCharId));

        userSpecyfication.checkForCurrentUser(userCharacteristics);

        List<User> userList = userRepository.findAll(userSpecyfication);

        userList.remove(currentUser);

        List<User> userListWithPreferedGener = new ArrayList<>();

        for (User user : userList) {
            String currentUserGender = currentUser.getGender();
            UserCharacteristic preferdGender = userCharacteristicsRepository.findByUserIdAndCharId(user.getId(), 16);
            if (preferdGender != null) {
                if (currentUserGender.equals("O") && preferdGender.getVal().equals("O")) {
                    userListWithPreferedGener.add(user);
                } else if (currentUserGender.equals(preferdGender.getVal()) || preferdGender.getVal().equals("O")) {
                    userListWithPreferedGener.add(user);
                }
            }
        }


        List<AbstractMap.SimpleEntry<User, Double>> userListWithScore = new ArrayList<>();

        //mamy lsite wszytskich poza obecnie zalogowanym userem
        for (User user : userListWithPreferedGener) {
            //Dla kazdego usera, brac jego cechy i porownywac z cechami zalogowanego usera i odejmowac od siebie w wartosci bezwzglednej
            Integer score = 0;
            Double average = 0.0;
            Integer other_user_id = user.getId();
            Integer current_user_id = currentUser.getId();

            //Obliczanie sumy wartosci bezwzglednych z charkterow uzytkownikow
            for (int i = 1; i <= 7; i++) {
                score += Math.abs(Integer.parseInt(userCharacteristicsRepository.findByUserIdAndCharId(other_user_id, i).getVal()) - Integer.parseInt(userCharacteristicsRepository.findByUserIdAndCharId(current_user_id, i).getVal()));
            }

            average = 5 - (score * 1.25/ 7.0);
            userListWithScore.add(new AbstractMap.SimpleEntry<>(user, average));
            System.out.println("Score: " + Math.round(average * 10.0) / 10.0 + " For: " + user.getFirstname());
        }

        //Sortowanie najabardziej dospasowanych po wyniku z ankiet
        Collections.sort(userListWithScore, new SimpleEntryComparator());

        //przekazywanie sredniej z uzytkownika przesylane do frontendu do 'gwiazdek'

        List<SearchDTO> searchDTOS = new ArrayList<>();
        SearchDTO searchDTO = null;
        Optional<Activity> activity = null;
        Boolean online = false;
        Optional<Photo> optionalPhoto = null;
        byte[] photo =  null;
        User user = null;


        //Liste z kluczem i waartoscia
        for (AbstractMap.SimpleEntry<User, Double> key : userListWithScore) {
            user = key.getKey();

            activity= activityRepository.findByEmail(user.getEmail());
            if (activity.isPresent()) {
                online = userService.isActive(activity.get());
            } else {
                System.err.println("There is no such thing as activity");
            }
            optionalPhoto = photoRepository.findByUserId(user.getId());
            if(optionalPhoto.isPresent()){
                photo = optionalPhoto.get().getData();
            }else{
                photo = null;
            }


            searchDTO = new SearchDTO(user.getId(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getAge(),
                    user.getGender(),
                    online,
                    photo,
                    key.getValue(),
                    user.getDescription());
            searchDTOS.add(searchDTO);
        }
        userListWithScore.clear();


        return new ResponseEntity<>(searchDTOS, HttpStatus.OK);
    }

}
