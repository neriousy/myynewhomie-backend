package com.druzynav.models.comments;

import com.druzynav.models.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "comments")
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_author_id")
    private User commentAuthor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commented_user_id")
    private User commentedUser;

    private String commentText;

    private LocalDateTime date;

    private Integer rating;
}
