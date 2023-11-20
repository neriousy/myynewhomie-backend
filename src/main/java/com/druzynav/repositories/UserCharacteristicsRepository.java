package com.druzynav.repositories;

import com.druzynav.models.user.User;
import com.druzynav.models.userCharacteristic.UserCharId;
import com.druzynav.models.userCharacteristic.UserCharacteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCharacteristicsRepository extends JpaRepository<UserCharacteristic, UserCharId> {

    UserCharacteristic findByUserIdAndCharId(Integer UserId, Integer CharId);

    List<UserCharacteristic> findByUserId(Integer UserId);
}
