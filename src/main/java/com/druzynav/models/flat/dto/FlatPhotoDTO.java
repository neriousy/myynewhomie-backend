package com.druzynav.models.flat.dto;

import com.druzynav.models.flat.Flat;
import com.druzynav.models.flat.FlatPhoto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlatPhotoDTO {
    private Integer id;
    private Integer flatId;
    private String fileName;
    private byte[] data;

    public static FlatPhotoDTO from(FlatPhoto flatPhoto) {
        return FlatPhotoDTO.builder()
                .id(flatPhoto.getId())
                .flatId(flatPhoto.getFlat().getId())
                .fileName(flatPhoto.getFile_name())
                .data(flatPhoto.getData())
                .build();
    }

    public FlatPhoto toFlatPhoto(Flat flat) {
        return FlatPhoto.builder()
                .id(this.id)
                .flat(flat)
                .file_name(this.fileName)
                .data(this.data)
                .build();
    }
}

