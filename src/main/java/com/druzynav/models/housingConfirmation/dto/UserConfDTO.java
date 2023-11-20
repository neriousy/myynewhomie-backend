package com.druzynav.models.housingConfirmation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public
class UserConfDTO {
    private Integer userId;
    private String firstname;
    private String lastname;
    private Integer age;
    private String gender;
    private byte[] photo;
}
