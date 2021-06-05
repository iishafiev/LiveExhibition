package com.hse.cs.ce.LiveExhibition.Repositories;

import com.hse.cs.ce.LiveExhibition.Models.ExhibitionTag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ExhibitionTagRepository extends CrudRepository<ExhibitionTag, Long> {
    @Query(value = "SELECT tag.* FROM Exhibition_Tag tag WHERE tag.Name = ?1 ",
            nativeQuery = true)
    Optional<ExhibitionTag> findByName(String Name);
}
