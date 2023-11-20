package com.druzynav.models.characteristic;

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
@Table(name = "characteristics")
public class Characteristic {
    @Id
    @GeneratedValue
    private Integer id;
    private String charname;


}
