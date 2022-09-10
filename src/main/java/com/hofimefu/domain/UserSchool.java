package com.hofimefu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hofimefu.domain.enumeration.SchoolStatus;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A UserSchool.
 */
@Entity
@Table(name = "user_school")
public class UserSchool implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SchoolStatus status;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "userSchools" }, allowSetters = true)
    private School school;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserSchool id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SchoolStatus getStatus() {
        return this.status;
    }

    public UserSchool status(SchoolStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(SchoolStatus status) {
        this.status = status;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserSchool user(User user) {
        this.setUser(user);
        return this;
    }

    public School getSchool() {
        return this.school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public UserSchool school(School school) {
        this.setSchool(school);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSchool)) {
            return false;
        }
        return id != null && id.equals(((UserSchool) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSchool{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
