package com.druzynav.models.flat;

import com.druzynav.models.photo.Photo;
import com.druzynav.models.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

// 1. Opis mieszkania
// 2. Lokalizacja
// 3. Zdjęcia
// 4. Ile osob w mieszkaniu
// 5. Ile pokoi ma mieszkanie

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "flat")
public class Flat {

    @Id
    @GeneratedValue
    private Integer id;

    // Opis mieszkania
    private String description;

    // Lokalizacja
    private Double latitude;
    private Double longitude;

    @OneToOne(optional = true)
    @JoinColumn(name = "user_id", unique = true)
    private User user;


    // Zdjęcia
    @OneToMany(mappedBy = "flat")
    private List<FlatPhoto> photos;

    // Ile osob w mieszkaniu
    private Integer people_count;

    // Ile pokoi ma mieszkanie
    private Integer room_count;
    private Integer search_option;

    @Override
    public String toString() {
        return "Flat{" +
                "id=" + id +
                // Add other properties here for meaningful representation
                '}';
    }

}
