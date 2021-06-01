package com.hse.cs.ce.LiveExhibition.Repositories;

import com.hse.cs.ce.LiveExhibition.Models.Exhibitions;
import com.hse.cs.ce.LiveExhibition.Models.Exhibitors;
import com.hse.cs.ce.LiveExhibition.Models.ExhibitorsApplication;
import com.hse.cs.ce.LiveExhibition.Models.Organizers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ExhibitorsApplicationRepository extends JpaRepository<ExhibitorsApplication, Long> {
    @Query(value = "SELECT apps.* FROM Exhibitors_Application apps " +
            "JOIN Exhibitions exh ON apps.exhibition_id = exh.id " +
            "JOIN Organizers org ON exh.organizer_id = org.id " +
            "WHERE org.email = ?1",
            nativeQuery = true)
    List<ExhibitorsApplication> findApplicationByOrganizerEmail(String email);

    @Query(value = "SELECT apps.* FROM Exhibitors_Application apps " +
            "JOIN Exhibitions exh ON apps.exhibition_id = exh.id " +
            "JOIN Organizers org ON exh.organizer_id = org.id " +
            "WHERE org.email = ?1 AND apps.Accepted = 0",
            nativeQuery = true)
    List<ExhibitorsApplication> findApplicationByOrganizerEmailNotAccepted(String email);

    @Query(value = "SELECT apps.* FROM Exhibitors_Application apps WHERE (apps.exhibitor_id = ?1) AND (apps.Accepted = 0)",
            nativeQuery = true)
    List<ExhibitorsApplication> findNotAcceptedApplicationByExhibitor(Long id);

    @Query(value = "SELECT apps.* FROM Exhibitors_Application apps WHERE (apps.exhibitor_id = ?1) AND (apps.Accepted = 1)",
            nativeQuery = true)
    List<ExhibitorsApplication> findAcceptedApplicationByExhibitor(Long id);

    @Query(value = "SELECT apps.* FROM Exhibitors_Application apps WHERE (apps.exhibition_id = ?1) AND (apps.Accepted = 1)",
            nativeQuery = true)
    List<ExhibitorsApplication> findAcceptedByExhibition(Long id);
}
