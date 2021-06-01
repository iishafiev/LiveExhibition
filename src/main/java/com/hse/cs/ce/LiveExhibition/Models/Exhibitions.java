package com.hse.cs.ce.LiveExhibition.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity
public class Exhibitions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String Name;


    @ManyToOne
    @JoinColumn(name="organizer_id", nullable=false)
    private Organizers organizer;

    private Date BeginningDate;

    private Date EndDate;

    private boolean IsApplication;

    private String ShortDescription;

    private String Description;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="tag_Name")
    private ExhibitionTag tag;

    public ExhibitionTag getTag() {
        return tag;
    }

    public void setTag(ExhibitionTag tag) {
        this.tag = tag;
    }
    /*@JsonManagedReference
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "exhibitions_exhibitors",
            joinColumns = @JoinColumn(name = "exhibition_id"),
            inverseJoinColumns = @JoinColumn(name = "exhibitor_id")
    )
    private List<Exhibitors> exhibitors;*/

    @OneToMany(mappedBy="Exhibition")
    private Set<ExhibitorsApplication> Applications;

    @OneToMany(mappedBy="exhibition")
    private Set<VisitorsFavouriteExhibitions> visitorsFavouriteExhibitions;

    public Set<VisitorsFavouriteExhibitions> getVisitorsFavouriteExhibitions() {
        return visitorsFavouriteExhibitions;
    }

    public void setVisitorsFavouriteExhibitions(Set<VisitorsFavouriteExhibitions> visitorsFavouriteExhibitions) {
        this.visitorsFavouriteExhibitions = visitorsFavouriteExhibitions;
    }

    /*public void addExhibitor(Exhibitors exhibitor){
        this.exhibitors.add(exhibitor);
        exhibitor.getExhibitions().add(this);
    }
    public void removeexhibitor(Exhibitors exhibitor){
        this.exhibitors.remove(exhibitor);
        exhibitor.getExhibitions().remove(this);
    }*/

    public Exhibitions() {
    }

    public Exhibitions(String name, String shortDescription, String description, Date beginningDate, Date endDate, Organizers organizer, ExhibitionTag tag) {
        Name = name;
        this.organizer = organizer;
        BeginningDate = beginningDate;
        EndDate = endDate;
        ShortDescription = shortDescription;
        Description = description;
        IsApplication = true;
        this.tag = tag;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBeginningDate() {
        return BeginningDate;
    }

    public void setBeginningDate(Date beginningDate) {
        BeginningDate = beginningDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = endDate;
    }

    public boolean isApplication() {
        return IsApplication;
    }

    public void setApplication(boolean application) {
        IsApplication = application;
    }

    public Organizers getOrganizer() {
        return this.organizer;
    }

    public void setOrganizer(Organizers organizer) {
        this.organizer = organizer;
    }

    public String getShortDescription() {
        return ShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        ShortDescription = shortDescription;
    }

    /*public List<Exhibitors> getExhibitors() {
        return exhibitors;
    }

    public void setExhibitors(List<Exhibitors> exhibitors) {
        this.exhibitors = exhibitors;
    }*/

    public Set<ExhibitorsApplication> getApplications() {
        return Applications;
    }

    public void setApplications(Set<ExhibitorsApplication> applications) {
        Applications = applications;
    }
}
