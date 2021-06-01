package com.hse.cs.ce.LiveExhibition.Repositories;

import com.hse.cs.ce.LiveExhibition.Models.Exhibitions;
import com.hse.cs.ce.LiveExhibition.Models.Exhibitors;
import com.hse.cs.ce.LiveExhibition.Models.Organizers;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExhibitionsRepository extends CrudRepository<Exhibitions, Long> {
    List<Exhibitions> findByOrganizer(Organizers organizer);
    @Query(value = "SELECT ee.* FROM exhibitions_exhibitors ee WHERE ee.exhibitor_id = ?1",
            nativeQuery = true)
    List<Exhibitions> findExhibitionsByExhibitorsId(Long id);

    @Query(value = "SELECT ee.* FROM exhibitions ee WHERE ee.is_application = 0",
            nativeQuery = true)
    List<Exhibitions> findAllAccepted();
    @Query(value = "SELECT  e.id, e.name, e.short_description, e.description, e.beginning_date, e.end_date FROM exhibitions e WHERE e.is_application = 0",
            nativeQuery = true)
    List<Object> findAllExhibitionsInfo();
}
