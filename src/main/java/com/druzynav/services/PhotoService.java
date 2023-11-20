package com.druzynav.services;

import com.druzynav.models.photo.Photo;
import com.druzynav.models.user.User;
import com.druzynav.repositories.PhotoRepository;
import com.druzynav.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

    public void savePhoto(MultipartFile file, Integer id) throws IOException {
        Optional<Photo> optionalPhoto = photoRepository.findByUserId(id);
        Optional<User> optionalUser = userRepository.findById(id);
        Photo photo;
        if(optionalUser.isEmpty()){
            throw new RuntimeException();
        }

        User user = optionalUser.get();

        byte[] data = file.getBytes();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        if(optionalPhoto.isPresent()){
            photo = optionalPhoto.get();
            photo.setData(data);
            photo.setName(fileName);
        }else{
            photo = Photo.builder()
            .user(user)
            .name(fileName)
            .data(data)
            .build();
        }
        photoRepository.save(photo);
    }

    public Optional<Photo> getPhotoById(Integer id) {
        return photoRepository.findByUserId(id);
    }

    @Transactional
    public void deletePhoto(Integer id){
        Optional<Photo> optionalPhoto = photoRepository.findByUserId(id);
        Photo photo = null;
        if(optionalPhoto.isPresent()){
            photo = optionalPhoto.get();
            photoRepository.deleteById(photo.getId());
            System.out.println("deleted");

        }
    }
}

