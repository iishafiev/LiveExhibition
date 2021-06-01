package com.hse.cs.ce.LiveExhibition.Repositories;

import com.hse.cs.ce.LiveExhibition.Models.Exhibitors;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ExhibitorsRepository extends CrudRepository<Exhibitors, Long> {
    Optional<Exhibitors> findByEmail(String email);
}
