package com.hofimefu.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Friend.
 */
@Entity
@Table(name = "friend")
public class Friend implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private FriendStatus status;

    @ManyToOne
    private User user1;

    @ManyToOne
    private User user2;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Friend id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FriendStatus getStatus() {
        return this.status;
    }

    public void setStatus(FriendStatus friendStatus) {
        this.status = friendStatus;
    }

    public Friend status(FriendStatus friendStatus) {
        this.setStatus(friendStatus);
        return this;
    }

    public User getUser1() {
        return this.user1;
    }

    public void setUser1(User user) {
        this.user1 = user;
    }

    public Friend user1(User user) {
        this.setUser1(user);
        return this;
    }

    public User getUser2() {
        return this.user2;
    }

    public void setUser2(User user) {
        this.user2 = user;
    }

    public Friend user2(User user) {
        this.setUser2(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Friend)) {
            return false;
        }
        return id != null && id.equals(((Friend) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Friend{" +
            "id=" + getId() +
            "}";
    }
}
