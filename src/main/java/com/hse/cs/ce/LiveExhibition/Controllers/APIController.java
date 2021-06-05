package com.hse.cs.ce.LiveExhibition.Controllers;

import com.amazonaws.services.apigateway.model.Op;
import com.hse.cs.ce.LiveExhibition.Models.*;
import com.hse.cs.ce.LiveExhibition.Repositories.*;
import com.hse.cs.ce.LiveExhibition.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired
    private ExhibitionsRepository exhibitionsRepository;

    @Autowired
    private OrganizersRepository organizersRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VisitorsRepository visitorsRepository;
    @Autowired
    private ExhibitorsRepository exhibitorsRepository;
    @Autowired
    private VisitorsFavouriteExhibitionsRepository visitorsFavouriteExhibitionsRepository;
    @Autowired
    private UserService userService;
    private final JdbcOperations jdbcOperations;

    public APIController(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @GetMapping(path = "/getAllExhibitions",  produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Exhibitions> getAllExhibitions() {
        List<Exhibitions> exhibitions = exhibitionsRepository.findAllExhibitionsInfo();
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

    @GetMapping(path = "/getExhibition/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Exhibitions getExhibitionById(@PathVariable(name = "id") String exhId) {
        Optional<Exhibitions> exhibition = exhibitionsRepository.findById(Long.parseLong(exhId));
        exhibition.get().getOrganizer().getExhibitions().clear();
        exhibition.get().getApplications().removeIf(i -> !i.Accepted());
        for (ExhibitorsApplication application:exhibition.get().getApplications()) {
            application.getExhibitor().getApplications().clear();
            application.getExhibitor().getGoods().clear();
        }
        exhibition.get().getVisitorsFavouriteExhibitions().clear();
        return exhibition.get();
    }
    @GetMapping(path = "/getExhibitionsByTag/{tag}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Exhibitions> getExhibitionsByTag(@PathVariable(name = "tag") String tagName) {
        List<Exhibitions> exhibitions;
        if(tagName.equals(""))
            exhibitions = exhibitionsRepository.findAllExhibitionsInfo();
        else
            exhibitions = exhibitionsRepository.findExhibitionsByTag(tagName);

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

    @GetMapping(path = "/getExhibitionsByName/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Exhibitions> getExhibitionsByName(@PathVariable(name = "name") String exhName) {
        List<Exhibitions> exhibitions;
        if(exhName.equals(""))
            exhibitions = exhibitionsRepository.findAllExhibitionsInfo();
        else
            exhibitions = exhibitionsRepository.findExhibitionsByName(exhName);

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

    @GetMapping(path = "/getFavouriteExhibitionsByUserId/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Exhibitions> getFavouriteExhibitionsByUserId(@PathVariable(name = "userId") String userId) {
        List<Exhibitions> exhibitions = new ArrayList<>();
        List<VisitorsFavouriteExhibitions> favs = visitorsFavouriteExhibitionsRepository.getFavouritesByVisitorId(Long.parseLong(userId));
        for (VisitorsFavouriteExhibitions fav: favs) {
            exhibitions.add(fav.getExhibition());
        }

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




    @GetMapping(path = "/getExhibitorById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Exhibitors getExhibitorById(@PathVariable(name = "id") String exhId) {
        Optional<Exhibitors> exhibitor = exhibitorsRepository.findById(Long.parseLong(exhId));
        exhibitor.get().getApplications().clear();
        return exhibitor.get();
    }

    @GetMapping(path = "/isExhibitionInFavourites/{exhId}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VisitorsFavouriteExhibitions isExhibitionInFavourites(@PathVariable(name = "exhId") String exhId, @PathVariable(name = "userId") String userId) {
        Optional<VisitorsFavouriteExhibitions> exh = visitorsFavouriteExhibitionsRepository.isPresentByIds(Long.parseLong(exhId), Long.parseLong(userId));
        if (exh.isPresent())

        {
            exh.get().setVisitor(null);
            exh.get().setExhibition(null);
            return exh.get();
        }
        else
            return new VisitorsFavouriteExhibitions();
    }

    @GetMapping(path = "/addToFavourites/{exhId}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VisitorsFavouriteExhibitions addToFavourites(@PathVariable(name = "exhId") String exhId, @PathVariable(name = "userId") String userId) {
        Optional<VisitorsFavouriteExhibitions> exh = visitorsFavouriteExhibitionsRepository.isPresentByIds(Long.parseLong(exhId), Long.parseLong(userId));
        if (exh.isPresent())
            return exh.get();
        else
        {
            Optional<Visitors> visitor = visitorsRepository.findById(Long.parseLong(userId));
            Optional<Exhibitions> exhibition = exhibitionsRepository.findById(Long.parseLong(exhId));
            VisitorsFavouriteExhibitions exhfav = new VisitorsFavouriteExhibitions(visitor.get(), exhibition.get());
            visitorsFavouriteExhibitionsRepository.save(exhfav);
            exhfav.setExhibition(null);
            exhfav.setVisitor(null);
            return exhfav;
        }
    }

    @GetMapping(path = "/removeFromFavourites/{exhId}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VisitorsFavouriteExhibitions removeFromFavourites(@PathVariable(name = "exhId") String exhId, @PathVariable(name = "userId") String userId) {
        Optional<VisitorsFavouriteExhibitions> exh = visitorsFavouriteExhibitionsRepository.isPresentByIds(Long.parseLong(exhId), Long.parseLong(userId));
        if(exh.isPresent()) {
            visitorsFavouriteExhibitionsRepository.deleteById(exh.get().getId());
            return exh.get();
        }
        else
            return new VisitorsFavouriteExhibitions();
    }

    @PostMapping(path = "/getAuthUser", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Visitors getAuthUser(@RequestBody UserAuthData person) {
        System.out.println("post request " + person.getUsername() + " " + person.getPassword());
        Visitors visitor;

        Optional<User> user = userService.getVisitorByUsernameAndPassword(person.getUsername(), person.getPassword());
        if(user != null)
            visitor = visitorsRepository.findByEmail(person.getUsername()).get();
        else
            visitor = new Visitors();
        System.out.println(visitor);
        return visitor;
    }
}
