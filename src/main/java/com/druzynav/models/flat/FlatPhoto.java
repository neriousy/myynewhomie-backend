package com.druzynav.models.flat;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "flat_photos")
public class FlatPhoto {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "flat_id", nullable = false)
    private Flat flat;

    private String file_name;

    @Lob
    @JdbcTypeCode(Types.BINARY)
    @Column(name = "data")
    private byte[] data;

    public FlatPhoto(Flat flat, String fileName, byte[] data) {
        this.flat = flat;
        this.file_name = fileName;
        this.data = data;
    }
}
