package com.sg.startmeup.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sg.startmeup.domain.util.CustomLocalDateSerializer;
import com.sg.startmeup.domain.util.ISO8601LocalDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Idea.
 */
@Entity
@Table(name = "IDEA")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Idea implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "creation_date")
    private LocalDate creation_date;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "idea")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Estimation> estimations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(LocalDate creation_date) {
        this.creation_date = creation_date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Estimation> getEstimations() {
        return estimations;
    }

    public void setEstimations(Set<Estimation> estimations) {
        this.estimations = estimations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Idea idea = (Idea) o;

        if ( ! Objects.equals(id, idea.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Idea{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", description='" + description + "'" +
                ", creation_date='" + creation_date + "'" +
                '}';
    }
}
