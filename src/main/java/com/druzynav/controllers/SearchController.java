package com.druzynav.controllers;

import com.druzynav.models.user.dto.SearchDTO;
import com.druzynav.services.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
public class SearchController {
    @Autowired
    private SearchService searchService;

    @PostMapping("/api/v1/search")
    public ResponseEntity<List<SearchDTO>> search(){
        return searchService.searchTen();
    }
}
