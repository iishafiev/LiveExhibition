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
    @Query(value = "SELECT ee.* FROM exhibitions ee WHERE ee.is_application = 0 and CURRENT_DATE() > ee.beginning_date and CURRENT_DATE() < ee.end_date",
            nativeQuery = true)
    List<Exhibitions> findAllExhibitionsInfo();
    @Query(value = "SELECT ee.* FROM exhibitions ee JOIN exhibition_tag t ON ee.tag_name = t.id WHERE ee.is_application = 0 and CURRENT_DATE() > ee.beginning_date and CURRENT_DATE() < ee.end_date and t.name = ?1",
            nativeQuery = true)
    List<Exhibitions> findExhibitionsByTag(String tag);
    @Query(value = "SELECT ee.* FROM exhibitions ee WHERE ee.is_application = 0 and CURRENT_DATE() > ee.beginning_date and CURRENT_DATE() < ee.end_date and ee.name LIKE %?1%",
            nativeQuery = true)
    List<Exhibitions> findExhibitionsByName(String name);
}
