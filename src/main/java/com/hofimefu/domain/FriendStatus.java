package com.hofimefu.domain;

import com.hofimefu.domain.enumeration.FriendshipStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A FriendStatus.
 */
@Entity
@Table(name = "friend_status")
public class FriendStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "created")
    private ZonedDateTime created;

    @Column(name = "last_changed")
    private ZonedDateTime lastChanged;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FriendshipStatus status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FriendStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public FriendStatus created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getLastChanged() {
        return this.lastChanged;
    }

    public FriendStatus lastChanged(ZonedDateTime lastChanged) {
        this.setLastChanged(lastChanged);
        return this;
    }

    public void setLastChanged(ZonedDateTime lastChanged) {
        this.lastChanged = lastChanged;
    }

    public FriendshipStatus getStatus() {
        return this.status;
    }

    public FriendStatus status(FriendshipStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FriendStatus)) {
            return false;
        }
        return id != null && id.equals(((FriendStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FriendStatus{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", lastChanged='" + getLastChanged() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
