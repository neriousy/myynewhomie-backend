package com.druzynav.models.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDTO {
    private Integer comment_author_id;
    private Integer commented_user_id;
    private String comment_text;
    private Integer rating;
}
