package com.druzynav.controllers;

import com.druzynav.models.search.SearchDetailsService;
import com.druzynav.models.user.dto.SearchCriteriaDTO;
import com.druzynav.models.user.dto.SearchCriteriaExtendedDTO;
import com.druzynav.models.user.dto.SearchDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
public class TestController{

    private SearchDetailsService searchDetailsService;

    @PostMapping("/api/v1/test_search")
    public ResponseEntity<List<SearchDTO>> search_test(@RequestBody SearchCriteriaDTO searchCriteriaDTO, HttpServletRequest request){
        return searchDetailsService.searchSpecificUsers(searchCriteriaDTO, request);
    }

    @PostMapping("/api/v1/extended_search")
    public ResponseEntity<List<SearchDTO>> extended_search(@RequestBody SearchCriteriaExtendedDTO searchCriteriaExtendedDTO, HttpServletRequest request){
        return searchDetailsService.searchSpecificUsersWithFilters(searchCriteriaExtendedDTO, request);
    }

    @GetMapping("/api/v1/similar_users")
    public ResponseEntity<List<SearchDTO>> similar_users(HttpServletRequest request){
        return searchDetailsService.searchSimilarUser(request);
    }
}
