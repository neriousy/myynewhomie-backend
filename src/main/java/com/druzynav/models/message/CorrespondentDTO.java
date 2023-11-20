package com.druzynav.models.message;

import com.druzynav.models.message.dto.DataBetweenUsersDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Setter
public class CorrespondentDTO {
    private Integer id;
    private String name;
    private byte[] photo;
    List<DataBetweenUsersDTO> messages;
    private boolean online;
}
