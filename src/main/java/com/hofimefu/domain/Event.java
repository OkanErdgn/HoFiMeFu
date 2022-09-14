package com.hofimefu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Size(max = 50)
    @Column(name = "header", length = 50)
    private String header;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created")
    private ZonedDateTime created;

    @Column(name = "planned")
    private ZonedDateTime planned;

    @ManyToOne
    private User createdBy;

    @OneToMany(mappedBy = "event")
    @JsonIgnoreProperties(value = { "event", "users" }, allowSetters = true)
    private Set<EvetUser> evetUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Event id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Event latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Event longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getHeader() {
        return this.header;
    }

    public Event header(String header) {
        this.setHeader(header);
        return this;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return this.description;
    }

    public Event description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public Event created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getPlanned() {
        return this.planned;
    }

    public Event planned(ZonedDateTime planned) {
        this.setPlanned(planned);
        return this;
    }

    public void setPlanned(ZonedDateTime planned) {
        this.planned = planned;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public Event createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public Set<EvetUser> getEvetUsers() {
        return this.evetUsers;
    }

    public void setEvetUsers(Set<EvetUser> evetUsers) {
        if (this.evetUsers != null) {
            this.evetUsers.forEach(i -> i.setEvent(null));
        }
        if (evetUsers != null) {
            evetUsers.forEach(i -> i.setEvent(this));
        }
        this.evetUsers = evetUsers;
    }

    public Event evetUsers(Set<EvetUser> evetUsers) {
        this.setEvetUsers(evetUsers);
        return this;
    }

    public Event addEvetUser(EvetUser evetUser) {
        this.evetUsers.add(evetUser);
        evetUser.setEvent(this);
        return this;
    }

    public Event removeEvetUser(EvetUser evetUser) {
        this.evetUsers.remove(evetUser);
        evetUser.setEvent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        return id != null && id.equals(((Event) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", header='" + getHeader() + "'" +
            ", description='" + getDescription() + "'" +
            ", created='" + getCreated() + "'" +
            ", planned='" + getPlanned() + "'" +
            "}";
    }
}
