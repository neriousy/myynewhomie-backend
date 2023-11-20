package com.druzynav.models.flat.dto;

import com.druzynav.models.user.User;
import lombok.*;
import org.springframework.core.io.InputStreamResource;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FlatDTO {
    String description;
    Double latitude;
    Double longitude;
    Integer numberOfPeople;
    Integer numberOfRooms;
    Integer searchOption;
    List<FlatPhotoDTO> photos = null;
}
