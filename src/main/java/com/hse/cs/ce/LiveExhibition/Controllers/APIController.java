package com.hse.cs.ce.LiveExhibition.Controllers;

import com.amazonaws.services.apigateway.model.Op;
import com.hse.cs.ce.LiveExhibition.Models.Exhibitions;
import com.hse.cs.ce.LiveExhibition.Models.ExhibitorsApplication;
import com.hse.cs.ce.LiveExhibition.Repositories.ExhibitionsRepository;
import com.hse.cs.ce.LiveExhibition.Repositories.OrganizersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class APIController {

    @Autowired
    private ExhibitionsRepository exhibitionsRepository;

    @Autowired
    private OrganizersRepository organizersRepository;

    private final JdbcOperations jdbcOperations;

    public APIController(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @GetMapping(path = "/api/getAllExhibitions", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Exhibitions> getAllExhibitions() {
        List<Exhibitions> exhibitions = (List<Exhibitions>)exhibitionsRepository.findAll();
        for (Exhibitions exhibition: exhibitions) {
            exhibition.getOrganizer().getExhibitions().clear();
            exhibition.getApplications().removeIf(i -> !i.Accepted());
            for (ExhibitorsApplication application:exhibition.getApplications()) {
                application.getExhibitor().getApplications().clear();
                application.getExhibitor().getGoods().clear();
            }
            exhibition.getVisitorsFavouriteExhibitions().clear();
        }
        return exhibitions;
    }

    @GetMapping(path = "/api/getExhibition/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Exhibitions> getExhibitionById(@PathVariable(name = "id") String exhId) {
        Optional<Exhibitions> exhibition = exhibitionsRepository.findById(Long.parseLong(exhId));
        exhibition.get().getOrganizer().getExhibitions().clear();
        exhibition.get().getApplications().removeIf(i -> !i.Accepted());
        for (ExhibitorsApplication application:exhibition.get().getApplications()) {
            application.getExhibitor().getApplications().clear();
            application.getExhibitor().getGoods().clear();
        }
        exhibition.get().getVisitorsFavouriteExhibitions().clear();
        return exhibition;
    }

}
