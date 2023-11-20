package com.druzynav.models.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@Setter
public class DataBetweenUsersDTO {
    private Integer messageId;
    private Integer senderId;

    private String senderName;

    private Integer receiverId;

    private String receiverName;

    private String message;

    private LocalDateTime date;
}
