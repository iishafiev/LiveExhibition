package com.hse.cs.ce.LiveExhibition.Services;

import com.hse.cs.ce.LiveExhibition.Models.*;
import com.hse.cs.ce.LiveExhibition.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrganizersRepository organizersRepository;
    @Autowired
    ExhibitorsRepository exhibitorsRepository;
    @Autowired
    VisitorsRepository visitorsRepository;
    @Autowired
    ModeratorsRepository moderatorsRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        return user.get();
    }

    public Optional<User> getVisitorByUsernameAndPassword(String username, String password)
    {
        Optional<User> user = userRepository.findVisitorByUsername(username);
        if (user.isPresent() & bCryptPasswordEncoder.matches(password, user.get().getPassword()))
            return user;
        else return null;
    }

    public List<User> allUsers() {
        return (List)userRepository.findAll();
    }

    public boolean saveUser(User user) {
        Optional<User> userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB.isPresent()) {
            System.out.println(userFromDB.get());
            return false;
        }
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (user.getUsername().contains("moderator")) {
            user.setUserType("4");
            user.setRoles(Collections.singleton(new Role(2L, "ROLE_ADMIN")));
        }

        userRepository.save(user);
        System.out.println(user);
        String type = user.getUserType();
        if (type.equals("1"))
            organizersRepository.save(new Organizers(user.getFullName(), user.getCompany(), user.getUsername()));
        else if (type.equals("2"))
            exhibitorsRepository.save(new Exhibitors(user.getFullName(), user.getCompany(), user.getUsername()));
        else if (type.equals("3"))
            visitorsRepository.save(new Visitors(user.getFullName(), user.getUsername()));
        else if (type.equals("4"))
            moderatorsRepository.save(new Moderators(user.getFullName(), user.getUsername()));
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public List<User> usergetList(Long idMin) {
        return em.createQuery("SELECT u FROM User u WHERE u.id = :paramId", User.class)
                .setParameter("paramId", idMin).getResultList();
    }


}
