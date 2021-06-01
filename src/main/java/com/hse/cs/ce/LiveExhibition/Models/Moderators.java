package com.hse.cs.ce.LiveExhibition.Models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Moderators {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // Полное ФИО модератора
    private String FullName;

    // Адрес электронной почты модератора
    private String email;

    public Moderators() {
    }

    public Moderators(String fullName, String email) {
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
