package com.hse.cs.ce.LiveExhibition.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Set;

// Модель пользователя с ролью "организатор" для базы данных
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity
public class Organizers {

    // Идентификатор организатора, генерируемый автоматически
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // Полное ФИО организатора
    private String FullName;
    // Компания, которую представляет организатор
    private String Company;
    // Адрес электронной почты организатора
    private String email;
    @JsonBackReference
    @OneToMany(mappedBy="organizer")
    private Set<Exhibitions> Exhibitions;

    // Геттеры и сеттеры полей сущности
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
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

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public Set<com.hse.cs.ce.LiveExhibition.Models.Exhibitions> getExhibitions() {
        return Exhibitions;
    }

    public void setExhibitions(Set<com.hse.cs.ce.LiveExhibition.Models.Exhibitions> exhibitions) {
        Exhibitions = exhibitions;
    }

    public Organizers(String fullName, String company, String email) {
        FullName = fullName;
        Company = company;
        this.email = email;
    }

    public Organizers() {
    }
}
