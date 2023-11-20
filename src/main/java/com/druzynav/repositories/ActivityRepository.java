package com.druzynav.repositories;

import com.druzynav.models.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityRepository  extends JpaRepository<Activity, String> {
    Optional<Activity> findByEmail(String email);
}
