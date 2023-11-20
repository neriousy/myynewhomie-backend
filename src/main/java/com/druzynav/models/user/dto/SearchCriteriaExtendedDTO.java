package com.druzynav.models.user.dto;

import com.druzynav.models.flat.Options;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//TODO: Check if variables could be private and static

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteriaExtendedDTO extends SearchCriteriaDTO {
    boolean hasFlat;
    Integer search_option;
    Integer people_count;

}
