package com.druzynav.models.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@Setter
public class DataAboutUserDTO {
    private Integer senderId;

    private String senderName;

    private Integer receiverId;

    private String receiverName;

}
