package com.druzynav.models.housingConfirmation;

import com.druzynav.models.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "housing")
public class HousingConfirmation {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        @Column(name = "confirming_users")
        private Integer confirmingUser;
        @Column(name = "confirmed_by_users")
        private Integer confirmedByUser;
        private int housingStatus;
        private LocalDateTime statusDate;
        private String token;

    }



    // 1 Kacper
    // 2 Aneta
    // 3 Kazik

    // Zaprawszam -> Potwierdza
    // 1 <-> 2
    // 3 <-> 1

