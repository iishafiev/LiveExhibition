package com.hse.cs.ce.LiveExhibition.Repositories;

import com.hse.cs.ce.LiveExhibition.Models.VisitorsFavouriteExhibitions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface VisitorsFavouriteExhibitionsRepository extends CrudRepository<VisitorsFavouriteExhibitions, Long> {
}
