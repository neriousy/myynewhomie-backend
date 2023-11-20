package com.druzynav.repositories;

import com.druzynav.models.housingConfirmation.HousingConfirmation;
import com.druzynav.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HousingConfirmationRepository extends JpaRepository<HousingConfirmation, Integer> {
        List<HousingConfirmation> findByConfirmingUserAndConfirmedByUser(Integer confirmingUser, Integer confirmedByUser);

        List<HousingConfirmation> findByConfirmedByUserAndConfirmingUser(Integer confirmedByUser, Integer confirmingUser);

        Optional<HousingConfirmation> findByToken(String token);

        List<HousingConfirmation> findByConfirmingUserOrConfirmedByUser(Integer confirmingUser, Integer confirmedByUser);

        List<HousingConfirmation> findByConfirmingUserOrConfirmedByUserAndHousingStatus(Integer userId, Integer userId1, int i);
}


