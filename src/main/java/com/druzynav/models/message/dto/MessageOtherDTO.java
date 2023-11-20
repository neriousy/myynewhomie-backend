package com.druzynav.models.message.dto;

import com.druzynav.models.message.Status;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageOtherDTO {
    private Integer senderId;
    private Integer receiverId;
    private String message;
    private LocalDateTime date;

}
