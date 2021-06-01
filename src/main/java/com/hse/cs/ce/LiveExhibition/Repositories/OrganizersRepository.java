package com.hse.cs.ce.LiveExhibition.Repositories;

import com.hse.cs.ce.LiveExhibition.Models.Organizers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrganizersRepository extends CrudRepository<Organizers, Long> {
    Optional<Organizers> findByEmail(String email);
}
