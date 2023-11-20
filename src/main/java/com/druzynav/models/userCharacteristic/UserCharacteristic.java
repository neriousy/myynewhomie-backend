package com.druzynav.models.userCharacteristic;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_characteristics")
@IdClass(UserCharId.class)
public class UserCharacteristic {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "char_id")
    private Integer charId;

    private String val;

}
