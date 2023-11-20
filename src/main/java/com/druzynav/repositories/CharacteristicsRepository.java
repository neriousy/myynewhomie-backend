package com.druzynav.repositories;

import com.druzynav.models.characteristic.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacteristicsRepository extends JpaRepository<Characteristic, Integer> {
    Characteristic findByCharname(String Charname);
}