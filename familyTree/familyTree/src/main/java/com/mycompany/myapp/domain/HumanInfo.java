package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HumanInfo.
 */
@Entity
@Table(name = "human_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HumanInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "date_of_death")
    private LocalDate dateOfDeath;

    @NotNull
    @Min(value = 0)
    @Column(name = "places", nullable = false)
    private Integer places;

    @NotNull
    @Min(value = 0)
    @Column(name = "relatives", nullable = false)
    private Integer relatives;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HumanInfo id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public HumanInfo dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfDeath() {
        return this.dateOfDeath;
    }

    public HumanInfo dateOfDeath(LocalDate dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
        return this;
    }

    public void setDateOfDeath(LocalDate dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public Integer getPlaces() {
        return this.places;
    }

    public HumanInfo places(Integer places) {
        this.places = places;
        return this;
    }

    public void setPlaces(Integer places) {
        this.places = places;
    }

    public Integer getRelatives() {
        return this.relatives;
    }

    public HumanInfo relatives(Integer relatives) {
        this.relatives = relatives;
        return this;
    }

    public void setRelatives(Integer relatives) {
        this.relatives = relatives;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HumanInfo)) {
            return false;
        }
        return id != null && id.equals(((HumanInfo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HumanInfo{" +
            "id=" + getId() +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", dateOfDeath='" + getDateOfDeath() + "'" +
            ", places=" + getPlaces() +
            ", relatives=" + getRelatives() +
            "}";
    }
}
