package com.druzynav.models.user.dto;

import com.druzynav.models.characteristic.dto.CharacteristicsDTO;
import com.druzynav.models.flat.dto.FlatDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSpecificDTO {
    public SearchDTO searchDTO;
    public CharacteristicsDTO characteristicsDTO;
    public FlatDTO flatDTO;

    public UserSpecificDTO(SearchDTO searchDTO, CharacteristicsDTO characteristicsDTO) {
        this.searchDTO = searchDTO;
        this.characteristicsDTO = characteristicsDTO;
    }
}
