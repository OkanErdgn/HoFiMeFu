package com.hofimefu.domain;

import com.hofimefu.domain.enumeration.Language;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A UserConfig.
 */
@Entity
@Table(name = "user_config")
public class UserConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "share_location")
    private Boolean shareLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserConfig id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getShareLocation() {
        return this.shareLocation;
    }

    public UserConfig shareLocation(Boolean shareLocation) {
        this.setShareLocation(shareLocation);
        return this;
    }

    public void setShareLocation(Boolean shareLocation) {
        this.shareLocation = shareLocation;
    }

    public Language getLanguage() {
        return this.language;
    }

    public UserConfig language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserConfig user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserConfig)) {
            return false;
        }
        return id != null && id.equals(((UserConfig) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserConfig{" +
            "id=" + getId() +
            ", shareLocation='" + getShareLocation() + "'" +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}
