package com.druzynav.controllers;

import com.druzynav.models.comments.Comments;
import com.druzynav.models.comments.dto.CommentsDTO;
import com.druzynav.models.comments.dto.CommentsDataDTO;
import com.druzynav.services.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @PostMapping(value = "/api/v1/comments/add")
    public ResponseEntity<Comments> addComment(@RequestBody CommentsDTO commentsDTO){
        //Ewentualnie mozna pobrac id_obecenie zalogowanego uzytkownika przez header
        commentsService.saveComment(commentsDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping(value = "/api/v1/comments/get/{id}")
    public ResponseEntity<List<CommentsDataDTO>> getComments(@RequestParam("id") Integer id){
        List<CommentsDataDTO> comments = commentsService.getListOfComments(id);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping(value = "/api/v1/comments/delete/{id}")
    public ResponseEntity<String> deleteComment(@RequestParam("id") Integer id){
        commentsService.deleteComment(id);
        return ResponseEntity.ok("Usunieto komentarz o ID: " + id);
    }

}
