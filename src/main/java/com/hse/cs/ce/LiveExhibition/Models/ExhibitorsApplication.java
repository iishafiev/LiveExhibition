package com.hse.cs.ce.LiveExhibition.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity
public class ExhibitorsApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToOne
    @JoinColumn(name="exhibitor_id", nullable=false)
    private Exhibitors Exhibitor;


    @ManyToOne
    @JoinColumn(name="exhibition_id", nullable=false)
    private Exhibitions Exhibition;

    private String Description;

    private boolean Accepted;

    public ExhibitorsApplication() {
    }

    public ExhibitorsApplication(Exhibitors exhibitor, Exhibitions exhibition, String description) {
        Exhibitor = exhibitor;
        Exhibition = exhibition;
        Description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exhibitors getExhibitor() {
        return Exhibitor;
    }

    public void setExhibitor(Exhibitors exhibitor) {
        Exhibitor = exhibitor;
    }

    public Exhibitions getExhibition() {
        return Exhibition;
    }

    public void setExhibition(Exhibitions exhibition) {
        Exhibition = exhibition;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean Accepted() {
        return Accepted;
    }

    public void Accepted(boolean Accepted) {
        this.Accepted = Accepted;
    }
}
