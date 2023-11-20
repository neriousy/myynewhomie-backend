package com.druzynav.controllers;

import com.druzynav.models.characteristic.dto.AllMetricsDTO;
import com.druzynav.models.characteristic.dto.CharacteristicsDTO;
import com.druzynav.models.dto.DtoById;
import com.druzynav.models.userCharacteristic.UserCharacteristic;

import com.druzynav.services.FlatService;
import com.druzynav.services.UserCharacteristicService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
public class UserCharacteristicController {

    @Autowired
    private UserCharacteristicService userCharacteristicService;

    @Autowired
    private FlatService flatService;

    @PostMapping("/api/v1/getUserCharS")
    public ResponseEntity<?> getCharByIdShort(@RequestBody DtoById userId){
        return userCharacteristicService.userCharByUserIdShort(userId.getId());
    }

    @PostMapping("/api/v1/getUserChar")
    public ResponseEntity<?> getCharByIdLong(@RequestBody DtoById userId){
        return userCharacteristicService.userCharByUserIdLong(userId.getId());
    }

    @PostMapping("/api/v1/saveUserAllCharacteristics")
    public ResponseEntity<?> saveChar(HttpServletRequest request, @RequestBody AllMetricsDTO entity){
        userCharacteristicService.saveUserChar(entity);
        flatService.saveFlat(entity, request);
        return ResponseEntity.ok().body("Udalo sie zpaisac profil uzytkownika!");
    }
}
