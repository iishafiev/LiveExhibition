package com.hse.cs.ce.LiveExhibition.Repositories;

import com.hse.cs.ce.LiveExhibition.Models.Visitors;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VisitorsRepository extends CrudRepository<Visitors, Long> {
    Optional<Visitors> findByEmail(String username);
}
