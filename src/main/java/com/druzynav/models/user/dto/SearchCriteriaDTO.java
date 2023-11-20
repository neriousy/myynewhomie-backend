package com.druzynav.models.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//TODO: Check if variables could be private and static

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteriaDTO {
    public Integer age_from;
    public Integer age_to;

    //TODO: Zmienic na liste/enum
    public String city;

    public String gender;

    public boolean ifStudent;
    public boolean ifWorking;
    public boolean ifSmoking;
    public boolean ifDrinkingAlc;

}
