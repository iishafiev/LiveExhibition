package com.hse.cs.ce.LiveExhibition.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity
public class ExhibitorsGoods {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="exhibitor_id", nullable=false)
    private Exhibitors exhibitor;

    private String Name;

    private String Description;

    private String Photo;

    public ExhibitorsGoods() {
    }

    public ExhibitorsGoods(Exhibitors exhibitor, String name, String description, String photo) {
        this.exhibitor = exhibitor;
        Name = name;
        Description = description;
        Photo = photo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exhibitors getExhibitor() {
        return this.exhibitor;
    }

    public void setExhibitor(Exhibitors exhibitor) {
        this.exhibitor = exhibitor;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }
}

