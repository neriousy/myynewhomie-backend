package com.druzynav.controllers;

import com.druzynav.models.characteristic.Characteristic;
import com.druzynav.models.characteristic.dto.CharDTO;
import com.druzynav.models.characteristic.dto.CharacteristicsDTO;
import com.druzynav.repositories.CharacteristicsRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@AllArgsConstructor
public class CharacteristicController {
    @Autowired
    private CharacteristicsRepository characteristicsRepository;

    @PostMapping("/api/v1/getCharByName")
    public ResponseEntity<?> saveChar(@RequestBody CharDTO Charname){
        Characteristic charn = characteristicsRepository.findByCharname(Charname.getCharname());
        return ResponseEntity.ok(charn);

    }
}
