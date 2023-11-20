package com.druzynav.models.characteristic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacteristicsDTO {
    private Integer sleepTime;
    private Integer cooking;
    private Integer invitingFriends;
    private Integer timeSpentOutsideHome;
    private Integer characterType;
    private Integer talkativity;
    private Integer conciliatory;
    private Integer likesPets;
    private Integer hasPets;
    private Integer smokes;
    private Integer drinks;
    private Integer isStudent;
    private Integer works;
    private Integer acceptsPets;
    private Integer acceptsSmoking;
    private String preferedGender;
    private String livesIn;
    private Integer userId;
}
