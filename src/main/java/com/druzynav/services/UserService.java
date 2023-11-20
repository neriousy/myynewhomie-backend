package com.druzynav.services;

import com.druzynav.auth.JwtService;
import com.druzynav.auth.dto.TokenRequest;
import com.druzynav.models.activity.Activity;
import com.druzynav.models.characteristic.dto.CharacteristicsDTO;
import com.druzynav.models.photo.Photo;
import com.druzynav.models.user.Status;
import com.druzynav.models.user.User;
import com.druzynav.models.user.dto.SearchDTO;
import com.druzynav.models.user.dto.UserDTO;
import com.druzynav.models.user.dto.UserSpecificDTO;
import com.druzynav.repositories.ActivityRepository;
import com.druzynav.repositories.PhotoRepository;
import com.druzynav.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    public JwtService jwtService;
    @Autowired
    public UserRepository userRepository;

    @Autowired
    public ActivityRepository activityRepository;





    public UserDTO UserInfo(TokenRequest token){
        String email = jwtService.extractUsername(token.getToken());

        Optional<User> user = userRepository.findByEmail(email);
        UserDTO userResponse = new UserDTO();
        if(user.isPresent()){
            User _user = user.get();
            userResponse.setUserId(_user.getId());
            userResponse.setFirstname(_user.getFirstname());
            userResponse.setLastname(_user.getLastname());
            userResponse.setUsername(_user.getUsername());
            userResponse.setAge(_user.getAge());
            userResponse.setGender(_user.getGender());
            userResponse.setPhonenumber(_user.getPhonenumber());
            if (_user.getStatus().equals(Status.ACTIVE)) {
                System.err.println("Tu powinny byc dane o mieszkaniu");
                //Wyswietl informacje o mieszkaniu dane uzytkownika
            }
            userResponse.setDescription(_user.getDescription());System.out.println(_user.getDescription());
            userResponse.setStill_looking(_user.getStill_looking());
        }else{
            userResponse.setUserId(-1);
        }

        return userResponse;
    }

    public ResponseEntity<?> SaveUserInfo(UserDTO userDTO){
        Optional<User> userSearch = userRepository.findById(userDTO.getUserId());
        if(userSearch.isEmpty()){
            return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
        }
        User user = userSearch.get();

        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setGender(userDTO.getGender());
        user.setAge(userDTO.getAge());
        user.setPhonenumber(userDTO.getPhonenumber());
        user.setDescription(userDTO.getDescription());
        user.setStill_looking(userDTO.getStill_looking());

        userRepository.save(user);

        return new ResponseEntity<>("Saved", HttpStatus.OK);
    }

    public ResponseEntity<List<User>> getTenUsers(){
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    public Boolean isActive(Activity activity){
        return activity.getLastActive().isAfter(LocalDateTime.now().minusMinutes(2));
    }

    public ResponseEntity<String> updateActivity(String email){
        Optional<Activity> current = activityRepository.findByEmail(email);
        if(current.isPresent()){
            Activity activity = current.get();
            activity.setLastActive(LocalDateTime.now());

            activityRepository.save(activity);

            return new ResponseEntity<>("Saved", HttpStatus.OK);
        }

        return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);

    }

}
