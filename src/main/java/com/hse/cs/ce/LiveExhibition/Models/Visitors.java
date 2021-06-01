package com.hse.cs.ce.LiveExhibition.Models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity
public class Visitors {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String FullName;
    private String email;

    @OneToMany(mappedBy = "visitor")
    private List<VisitorsFavouriteExhibitions> visitorsFavouriteExhibitions;

    public Visitors() {
    }

    public Visitors(String fullName, String email) {
        FullName = fullName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
