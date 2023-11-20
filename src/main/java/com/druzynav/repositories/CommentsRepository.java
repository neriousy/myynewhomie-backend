package com.druzynav.repositories;

import com.druzynav.models.comments.Comments;
import com.druzynav.models.flat.Flat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Integer> {
    List<Comments> findAllByCommentedUserId(Integer id);
}