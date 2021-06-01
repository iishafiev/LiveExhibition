package com.hse.cs.ce.LiveExhibition.Repositories;

import com.hse.cs.ce.LiveExhibition.Models.Moderators;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ModeratorsRepository extends CrudRepository<Moderators, Long> {
    Optional<Moderators> findByEmail(String email);
}
