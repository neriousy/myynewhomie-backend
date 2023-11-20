package com.druzynav.services;

import com.druzynav.auth.JwtService;
import com.druzynav.auth.email.EmailSender;
import com.druzynav.auth.email.EmailService;
import com.druzynav.models.housingConfirmation.HousingConfirmation;
import com.druzynav.models.photo.Photo;
import com.druzynav.models.user.User;
import com.druzynav.models.housingConfirmation.dto.UserConfDTO;
import com.druzynav.repositories.HousingConfirmationRepository;
import com.druzynav.repositories.PhotoRepository;
import com.druzynav.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HousingConfirmationService {

    @Value("${app.domain}")
    private String domain;

    @Value("${frontend.domain}")
    private String frontend;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private HousingConfirmationRepository housingConfirmationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotoRepository photoRepository;

    public ResponseEntity confirmHousing(HttpServletRequest request, Integer otherUserId) throws MessagingException {

        //W ten sposob pozyskuje dane o obecnie zalagowanym uzytkowniku
        String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String username = jwtService.extractUsername(jwt);
        User currentUser = userRepository.findByEmail(username).get();
//        User currentUser = userRepository.findById(2).get();

        //W ten sposob pozyskuje dane o uzytkowniku, do ktorego chce wyslac zapytanie
        User otherUser = userRepository.findById(otherUserId).get();

        //Sprawdzenie

        boolean isUserPresent = false;

        //Musze sprawdzić czy taka para, uzytkowników nie ma juz potwierdzenia

        // W tym momencie, znajdujemy wszystkie potwierdzenia, ktore dotycza uzytkownika, ktory wysyla zapytanie
        // Dostajemy id osob, z którymi jest powiazany

        // To działa
        List<HousingConfirmation> fromSendingToReceiving = housingConfirmationRepository.findByConfirmingUserAndConfirmedByUser(currentUser.getId(), otherUserId);
        // To niestety nie!
        List<HousingConfirmation> fromReceivingToSending = housingConfirmationRepository.findByConfirmedByUserAndConfirmingUser(otherUserId, currentUser.getId());

        System.out.println("Current user: " + currentUser.toString());

        // Sprawdzam, czy w pobranej liscie uzytkownikow, jest uzytkownik, do ktorego chce wyslac zapytanie
        for (HousingConfirmation housingConfirmation : fromSendingToReceiving) {
            if (housingConfirmation.getConfirmedByUser() == otherUserId) {
                isUserPresent = true;
                break;
            }
        }

        for (HousingConfirmation housingConfirmation : fromReceivingToSending) {
            if (housingConfirmation.getConfirmingUser() == currentUser.getId()) {
                isUserPresent = true;
                break;
            }
        }

        // Jesli tak to zwroc odpowiedz
        if (isUserPresent) {
            System.out.println("Taka para juz istnieje");
            return ResponseEntity.badRequest().build();
        }

        //Jesli nie to tworzymy token
        try {
            String token = UUID.randomUUID().toString();

            HousingConfirmation housingConfirmation = new HousingConfirmation();
            housingConfirmation.setToken(token);
            housingConfirmation.setStatusDate(LocalDateTime.now());
            housingConfirmation.setHousingStatus(0); // Ustaw odpowiedni status mieszkania
            housingConfirmation.setConfirmingUser(currentUser.getId());
            housingConfirmation.setConfirmedByUser(otherUser.getId());

            housingConfirmationRepository.save(housingConfirmation);


//            URI link = URI.create(domain + "/api/v1/housing/confirm?token=" + token);
            URI link = URI.create(frontend + "confirmHousing?token=" + token);

            emailSender.sendRequestToConfirmHousing(otherUser.getEmail(), String.valueOf(link), currentUser.getFirstname(), currentUser.getLastname(), otherUser.getFirstname());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity("Mail z potwierdzeniem zostal wylsany", HttpStatus.CREATED);
    }

    @Transactional
    public String confirmHousingToken(String token){
        HousingConfirmation housingConfirmation = housingConfirmationRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if (housingConfirmation.getStatusDate().isBefore(LocalDateTime.now().minusHours(6))){
            return "expired";
        }

//        if(confirmationToken.getConfirmedAt() != null)
//            throw new IllegalStateException("email is already confirmed");

            housingConfirmation.setHousingStatus(1);
            housingConfirmation.setStatusDate(LocalDateTime.now());


        return "confirmed";
    }


    //
    public List<UserConfDTO>  getUsersRelatedToUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String username = jwtService.extractUsername(jwt);
        Optional<User> currentUser = Optional.of(userRepository.findByEmail(username).get());
        //Optional<User> currentUser = Optional.of(userRepository.findById(1).get());
        if (currentUser.isEmpty()) {
            return Collections.emptyList();
        }

        Integer userId = currentUser.get().getId();

        List<HousingConfirmation> confirmations = housingConfirmationRepository.findByConfirmingUserOrConfirmedByUserAndHousingStatus(userId, userId,1);
        List<Integer> relatedUsers = confirmations.stream()
                .flatMap(c -> Stream.of(c.getConfirmingUser(), c.getConfirmedByUser()))
                .distinct()
                .filter(id -> !Objects.equals(id, userId))
                .collect(Collectors.toList());

        List<UserConfDTO> userConfDTOS = new ArrayList<>();
        UserConfDTO userConfDTO = null;
        Optional<Photo> optionalPhoto = null;
        byte[] photo = null;

        for (Integer relatedUser : relatedUsers) {
            User user = userRepository.findById(relatedUser).get();

            optionalPhoto = photoRepository.findByUserId(relatedUser);
            if(optionalPhoto.isPresent()){
                photo = optionalPhoto.get().getData();
            }else{
                photo = null;
            }

            userConfDTO = new UserConfDTO(
                    user.getId(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getAge(),
                    user.getGender(),
                    photo
            );

            userConfDTOS.add(userConfDTO);
        }

        return userConfDTOS;
    }
}
