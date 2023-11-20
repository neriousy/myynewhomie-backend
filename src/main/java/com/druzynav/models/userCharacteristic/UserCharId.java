package com.druzynav.models.userCharacteristic;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class UserCharId implements Serializable {
    private static final long serialVersionUID = 2702030623316532366L;

    private Integer userId;
    private Integer charId;

}