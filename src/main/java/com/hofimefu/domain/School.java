package com.hofimefu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A School.
 */
@Entity
@Table(name = "school")
public class School implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "adress")
    private String adress;

    @Column(name = "email_domain")
    private String emailDomain;

    @OneToMany(mappedBy = "school")
    @JsonIgnoreProperties(value = { "user", "school" }, allowSetters = true)
    private Set<UserSchool> userSchools = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public School id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public School name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return this.adress;
    }

    public School adress(String adress) {
        this.setAdress(adress);
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getEmailDomain() {
        return this.emailDomain;
    }

    public School emailDomain(String emailDomain) {
        this.setEmailDomain(emailDomain);
        return this;
    }

    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
    }

    public Set<UserSchool> getUserSchools() {
        return this.userSchools;
    }

    public void setUserSchools(Set<UserSchool> userSchools) {
        if (this.userSchools != null) {
            this.userSchools.forEach(i -> i.setSchool(null));
        }
        if (userSchools != null) {
            userSchools.forEach(i -> i.setSchool(this));
        }
        this.userSchools = userSchools;
    }

    public School userSchools(Set<UserSchool> userSchools) {
        this.setUserSchools(userSchools);
        return this;
    }

    public School addUserSchool(UserSchool userSchool) {
        this.userSchools.add(userSchool);
        userSchool.setSchool(this);
        return this;
    }

    public School removeUserSchool(UserSchool userSchool) {
        this.userSchools.remove(userSchool);
        userSchool.setSchool(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof School)) {
            return false;
        }
        return id != null && id.equals(((School) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "School{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", adress='" + getAdress() + "'" +
            ", emailDomain='" + getEmailDomain() + "'" +
            "}";
    }
}
