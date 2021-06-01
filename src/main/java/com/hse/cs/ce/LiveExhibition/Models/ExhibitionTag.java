package com.hse.cs.ce.LiveExhibition.Models;

import com.hse.cs.ce.LiveExhibition.Repositories.ExhibitionTagRepository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ExhibitionTag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String Name;

    public ExhibitionTag() {
    }

    public ExhibitionTag(String tagName) {
        this.Name = tagName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return Name;
    }

    public void setTagName(String tagName) {
        this.Name = tagName;
    }
}
