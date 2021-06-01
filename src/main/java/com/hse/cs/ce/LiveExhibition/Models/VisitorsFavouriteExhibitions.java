package com.hse.cs.ce.LiveExhibition.Models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity
public class VisitorsFavouriteExhibitions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToOne
    @JoinColumn(name="visitor_id", nullable=false)
    private Visitors visitor;


    @ManyToOne
    @JoinColumn(name="exhibition_id", nullable=false)
    private Exhibitions exhibition;

    public VisitorsFavouriteExhibitions() {
    }

    public VisitorsFavouriteExhibitions(Visitors visitor, Exhibitions exhibition) {
        this.visitor = visitor;
        this.exhibition = exhibition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Visitors getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitors visitor) {
        this.visitor = visitor;
    }

    public Exhibitions getExhibition() {
        return exhibition;
    }

    public void setExhibition(Exhibitions exhibition) {
        this.exhibition = exhibition;
    }
}
