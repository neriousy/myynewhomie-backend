package com.druzynav.services;

import com.druzynav.models.characteristic.dto.CharacteristicsDTO;
import com.druzynav.models.flat.dto.FlatDTO;
import com.druzynav.models.user.dto.SearchDTO;
import com.druzynav.models.user.dto.UserSpecificDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSpecificService {
    @Autowired
    public SearchService searchService;

    @Autowired
    public UserCharacteristicService userCharacteristicService;

    @Autowired
    public FlatService flatService;

    public ResponseEntity<UserSpecificDTO> userSpecific(Integer userId){
        SearchDTO searchDTO = searchService.searchOne(userId);
        CharacteristicsDTO characteristicsDTO = userCharacteristicService.userCharacteristic(userId);
        FlatDTO flatDTO = flatService.searchOne(userId);

        UserSpecificDTO userSpecificDTO;
        if (flatDTO == null){
            userSpecificDTO = new UserSpecificDTO(searchDTO, characteristicsDTO);
        } else {
            userSpecificDTO = new UserSpecificDTO(searchDTO, characteristicsDTO, flatDTO);
        }
        return new ResponseEntity<>(userSpecificDTO, HttpStatus.OK);
    }
}
