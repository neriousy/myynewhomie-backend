package com.druzynav.models.housingConfirmation.dto;

import com.druzynav.models.user.User;
import lombok.*;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HousingConfirmationDTO {
    private List<User> confirmingUsers;
    private List<User> confirmedByUsers;
    private int housingStatus;
    private LocalDateTime statusDate;
    private String token;
}
