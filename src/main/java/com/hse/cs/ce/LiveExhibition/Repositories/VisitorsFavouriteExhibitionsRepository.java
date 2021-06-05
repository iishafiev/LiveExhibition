package com.hse.cs.ce.LiveExhibition.Repositories;

import com.hse.cs.ce.LiveExhibition.Models.ExhibitionTag;
import com.hse.cs.ce.LiveExhibition.Models.VisitorsFavouriteExhibitions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VisitorsFavouriteExhibitionsRepository extends CrudRepository<VisitorsFavouriteExhibitions, Long> {
    @Query(value = "SELECT vfe.* FROM visitors_favourite_exhibitions vfe WHERE vfe.exhibition_id = ?1 and vfe.visitor_id = ?2 ",
            nativeQuery = true)
    Optional<VisitorsFavouriteExhibitions> isPresentByIds(Long exhId, Long userId);

    @Query(value = "SELECT vfe.* FROM visitors_favourite_exhibitions vfe WHERE vfe.visitor_id = ?1",
            nativeQuery = true)
    List<VisitorsFavouriteExhibitions> getFavouritesByVisitorId(Long userId);
}
