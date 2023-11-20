package com.druzynav.repositories;

import com.druzynav.models.flat.Flat;
import com.druzynav.models.flat.FlatPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlatPhotosRepository extends JpaRepository<FlatPhoto, Integer> {
    Optional<List<FlatPhoto>> findAllByFlatId(Integer id);

    List<FlatPhoto> findByFlat(Flat flat);
}
