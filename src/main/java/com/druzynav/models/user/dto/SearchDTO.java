package com.druzynav.models.user.dto;

import com.druzynav.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//TODO: Check if variables could be private and static
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {
    public Integer id;
    public String firstname;
    public String lastname;
    public int age;
    public String gender;
    public boolean online;
    public byte[] photo;
    public Double score = -1.0;
    public String description;
}
