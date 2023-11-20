package com.druzynav.services;

import com.druzynav.models.activity.Activity;
import com.druzynav.models.photo.Photo;
import com.druzynav.models.user.User;
import com.druzynav.models.user.dto.SearchDTO;
import com.druzynav.repositories.ActivityRepository;
import com.druzynav.repositories.PhotoRepository;
import com.druzynav.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SearchService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserService userService;
    private final PhotoRepository photoRepository;


    public ResponseEntity<List<SearchDTO>> searchTen(){
        List<SearchDTO> searchDTOS = new ArrayList<>();
        List<User> users = userRepository.findAll();
        SearchDTO searchDTO = null;
        Optional<Activity> activity = null;
        Optional<Photo> optionalPhoto = null;
        byte[] photo =  null;
        Boolean online = false;
        for (User user: users
             ) {
            activity = activityRepository.findByEmail(user.getEmail());
            optionalPhoto = photoRepository.findByUserId(user.getId());
            if(optionalPhoto.isPresent()){
                photo = optionalPhoto.get().getData();
            }else{
                photo = null;
            }
            online = userService.isActive(activity.get());
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
        return new ResponseEntity<List<SearchDTO>>(searchDTOS,HttpStatus.OK);
    }

    public SearchDTO searchOne(Integer id){
        Optional<SearchDTO> optionalSearchDTO = null;
        Optional<User> optionalUser = userRepository.findById(id);
        SearchDTO searchDTO = null;
        Optional<Activity> activity = null;
        Optional<Photo> optionalPhoto = null;
        byte[] photo =  null;
        Boolean online = false;
        User user = null;
        if(optionalUser.isPresent()){
            user = optionalUser.get();

            activity = activityRepository.findByEmail(user.getEmail());
            optionalPhoto = photoRepository.findByUserId(user.getId());
            if(optionalPhoto.isPresent()){
                photo = optionalPhoto.get().getData();
            }else{
                    photo = null;
            }
            online = userService.isActive(activity.get());
            searchDTO = new SearchDTO(user.getId(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getAge(),
                    user.getGender(),
                    online,
                    photo,-1.0,
                    user.getDescription());
        }
        return searchDTO;
    }


}