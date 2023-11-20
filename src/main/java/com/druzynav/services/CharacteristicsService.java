package com.druzynav.services;

import com.druzynav.models.characteristic.Characteristic;
import com.druzynav.repositories.CharacteristicsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacteristicsService {
    @Autowired
    public CharacteristicsRepository characteristicsRepository;

    public void addChararcteristic(Characteristic characteristic){
        characteristicsRepository.save(characteristic);
    }
}
