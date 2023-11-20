package com.druzynav.models.photo;

import com.druzynav.models.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne(optional = true)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "name")
    private String name;

    @Lob
    @JdbcTypeCode(Types.BINARY)
    @Column(name = "data")
    private byte[] data;

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
