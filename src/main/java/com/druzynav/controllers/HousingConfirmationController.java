package com.druzynav.controllers;

import com.druzynav.models.housingConfirmation.dto.OnlyId;
import com.druzynav.models.housingConfirmation.dto.UserConfDTO;
import com.druzynav.services.HousingConfirmationService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class HousingConfirmationController {

    @Autowired
    private HousingConfirmationService housingConfirmationService;

    @PostMapping("/api/v1/housingConfirmation")
    public ResponseEntity confirmHousing(@RequestBody OnlyId onlyId, HttpServletRequest request) throws MessagingException {
        return housingConfirmationService.confirmHousing(request, onlyId.getUserId());

    }

    @GetMapping("/api/v1/housingConfirmation/confirm")
    public ResponseEntity<String> confirm(
            @RequestParam("token") String token
    ) {
        return ResponseEntity.ok(housingConfirmationService.confirmHousingToken(token));
    }

    @GetMapping("/api/v1/housingConfirmation/confirm/list/{id}")
    public ResponseEntity<List<UserConfDTO>> confirmList(HttpServletRequest request) {
        List<UserConfDTO> relatedUsers = housingConfirmationService.getUsersRelatedToUser(request);
        return ResponseEntity.ok(relatedUsers);
    }
}
