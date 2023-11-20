package com.druzynav.services;

import com.druzynav.models.comments.Comments;
import com.druzynav.models.comments.dto.CommentsDTO;
import com.druzynav.models.comments.dto.CommentsDataDTO;
import com.druzynav.models.user.User;
import com.druzynav.repositories.CommentsRepository;
import com.druzynav.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private UserRepository userRepository;

    // Metoda zapisujaca komentarz do bazy danych na podstawie danych z formularza
    public void saveComment(CommentsDTO commentsDTO) {
        User author = userRepository.findById(commentsDTO.getComment_author_id()).orElse(null);
        User commentedUser = userRepository.findById(commentsDTO.getCommented_user_id()).orElse(null);
        LocalDateTime date = LocalDateTime.now();

        //Sprawdzamy czy istnieja tacy uzytkownicy
        if (author == null) {
            System.out.println("Nie ma użytkownika o ID: " + author.getId());
            return;
        }

        if (commentedUser == null) {
            System.out.println("Nie ma użytkownika o ID: " + commentedUser.getId());
            return;
        }

        //Sprawdzamy czy zgadza sie zakres oceny
        if (commentsDTO.getRating() > 5 || commentsDTO.getRating() < 1) {
            System.out.println("Ocena musi być w przedziale od 1 do 5");
            return;
        }

        Comments comments = new Comments();
        comments.setCommentAuthor(author);
        comments.setCommentedUser(commentedUser);
        comments.setCommentText(commentsDTO.getComment_text());
        comments.setRating(commentsDTO.getRating());
        comments.setDate(date);

        commentsRepository.save(comments);
    }

    // Metoda pobierajaca liste komentarzy dla danego uzytkownika na podstawie jego id
    public List<CommentsDataDTO> getListOfComments(Integer id) {
        List<Comments> comments = commentsRepository.findAllByCommentedUserId(id);
        List<CommentsDataDTO> commentsData = new ArrayList<>();

        for (Comments comment : comments) {
            CommentsDataDTO commentData = new CommentsDataDTO();
            commentData.setComment_author_id(comment.getCommentAuthor().getId());
            commentData.setCommented_user_id(comment.getCommentedUser().getId());
            commentData.setComment_text(comment.getCommentText());
            commentData.setRating(comment.getRating());
            commentData.setDate(comment.getDate());
            commentsData.add(commentData);
        }

        return commentsData;
    }

    // Metoda usuwajaca komentarz o podanym id (id komentarza)
    public void deleteComment(Integer commentId) {
        Optional<Comments> optionalComment = commentsRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            commentsRepository.delete(optionalComment.get());
            System.out.println("Usunieto komentarz o ID: " + commentId);
        } else {
            System.out.println("Nie znaleziono komentarza o ID: " + commentId);
        }
    }
}
