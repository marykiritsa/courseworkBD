package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Places.
 */
@Entity
@Table(name = "places")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Places implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "place_of_birth", length = 50, nullable = false)
    private String placeOfBirth;

    @NotNull
    @Size(max = 50)
    @Column(name = "place_of_death", length = 50, nullable = false)
    private String placeOfDeath;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Places id(Long id) {
        this.id = id;
        return this;
    }

    public String getPlaceOfBirth() {
        return this.placeOfBirth;
    }

    public Places placeOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
        return this;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getPlaceOfDeath() {
        return this.placeOfDeath;
    }

    public Places placeOfDeath(String placeOfDeath) {
        this.placeOfDeath = placeOfDeath;
        return this;
    }

    public void setPlaceOfDeath(String placeOfDeath) {
        this.placeOfDeath = placeOfDeath;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Places)) {
            return false;
        }
        return id != null && id.equals(((Places) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Places{" +
            "id=" + getId() +
            ", placeOfBirth='" + getPlaceOfBirth() + "'" +
            ", placeOfDeath='" + getPlaceOfDeath() + "'" +
            "}";
    }
}
