package com.druzynav.repositories;

import com.druzynav.models.flat.Flat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlatRepository extends JpaRepository<Flat, Integer>{

    Flat findByUserId(Integer userId);
}
