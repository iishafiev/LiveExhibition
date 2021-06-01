package com.hse.cs.ce.LiveExhibition.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

// Модель пользователя с ролью "экспонент" для базы данных
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity
public class Exhibitors {

    // Идентификатор экспонента, генерируемый автоматически
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // Полное ФИО экспонента
    private String FullName;
    // Компания, которую представляет экспонент
    private String Company;
    // Адрес электронной почты экспонента
    private String email;

    private String Description;

    /*@JsonManagedReference
    @ManyToMany(mappedBy = "exhibitors")
    private List<Exhibitions> exhibitions;*/
    //@JsonManagedReference
    @OneToMany(mappedBy = "Exhibitor")
    private List<ExhibitorsApplication> Applications;

    @OneToMany(mappedBy = "exhibitor")
    private List<ExhibitorsGoods> Goods;

    public Exhibitors() {
    }

    public Exhibitors(String fullName, String company, String email) {
        FullName = fullName;
        Company = company;
        this.email = email;
    }

    // Геттеры и сеттеры полей сущности
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public List<Exhibitions> getExhibitions() {
        return exhibitions;
    }

    public void setExhibitions(List<Exhibitions> exhibitions) {
        this.exhibitions = exhibitions;
    }*/

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public List<ExhibitorsApplication> getApplications() {
        return Applications;
    }

    public void setApplications(List<ExhibitorsApplication> applications) {
        Applications = applications;
    }

    public List<ExhibitorsGoods> getGoods() {
        return Goods;
    }

    public void setGoods(List<ExhibitorsGoods> goods) {
        Goods = goods;
    }
}
