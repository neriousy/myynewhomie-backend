package com.druzynav.repositories;

import com.druzynav.models.activity.Activity;
import com.druzynav.models.photo.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    Optional<Photo> findByUserId(Integer userId);

    @Modifying
    @Query("DELETE FROM Photo p WHERE p.id = ?1")
    void deleteById(@Param("id") Integer id);
}

