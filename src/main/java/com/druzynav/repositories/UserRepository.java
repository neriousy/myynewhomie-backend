package com.druzynav.repositories;

import com.druzynav.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

    public interface UserRepository extends JpaRepository<User, Integer>,  JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Integer id);

    //List<User> findAll();

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableUser(String email);


}
