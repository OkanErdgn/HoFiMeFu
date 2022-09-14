package com.hofimefu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A EvetUser.
 */
@Entity
@Table(name = "evet_user")
public class EvetUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = { "createdBy", "evetUsers" }, allowSetters = true)
    private Event event;

    @ManyToMany
    @JoinTable(
        name = "rel_evet_user__user",
        joinColumns = @JoinColumn(name = "evet_user_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EvetUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public EvetUser event(Event event) {
        this.setEvent(event);
        return this;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public EvetUser users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public EvetUser addUser(User user) {
        this.users.add(user);
        return this;
    }

    public EvetUser removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EvetUser)) {
            return false;
        }
        return id != null && id.equals(((EvetUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvetUser{" +
            "id=" + getId() +
            "}";
    }
}
