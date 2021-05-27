package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TypeOfRelationship.
 */
@Entity
@Table(name = "type_of_relationship")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TypeOfRelationship implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "degree_of_kinship", length = 30, nullable = false)
    private String degreeOfKinship;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeOfRelationship id(Long id) {
        this.id = id;
        return this;
    }

    public String getDegreeOfKinship() {
        return this.degreeOfKinship;
    }

    public TypeOfRelationship degreeOfKinship(String degreeOfKinship) {
        this.degreeOfKinship = degreeOfKinship;
        return this;
    }

    public void setDegreeOfKinship(String degreeOfKinship) {
        this.degreeOfKinship = degreeOfKinship;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeOfRelationship)) {
            return false;
        }
        return id != null && id.equals(((TypeOfRelationship) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeOfRelationship{" +
            "id=" + getId() +
            ", degreeOfKinship='" + getDegreeOfKinship() + "'" +
            "}";
    }
}
