package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Relatives.
 */
@Entity
@Table(name = "relatives")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Relatives implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "surname", length = 20, nullable = false)
    private String surname;

    @NotNull
    @Size(max = 20)
    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @NotNull
    @Size(max = 20)
    @Column(name = "patronymic", length = 20, nullable = false)
    private String patronymic;

    @NotNull
    @Min(value = 0)
    @Column(name = "type_of_relationship", nullable = false)
    private Integer typeOfRelationship;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Relatives id(Long id) {
        this.id = id;
        return this;
    }

    public String getSurname() {
        return this.surname;
    }

    public Relatives surname(String surname) {
        this.surname = surname;
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return this.name;
    }

    public Relatives name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return this.patronymic;
    }

    public Relatives patronymic(String patronymic) {
        this.patronymic = patronymic;
        return this;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Integer getTypeOfRelationship() {
        return this.typeOfRelationship;
    }

    public Relatives typeOfRelationship(Integer typeOfRelationship) {
        this.typeOfRelationship = typeOfRelationship;
        return this;
    }

    public void setTypeOfRelationship(Integer typeOfRelationship) {
        this.typeOfRelationship = typeOfRelationship;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Relatives)) {
            return false;
        }
        return id != null && id.equals(((Relatives) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Relatives{" +
            "id=" + getId() +
            ", surname='" + getSurname() + "'" +
            ", name='" + getName() + "'" +
            ", patronymic='" + getPatronymic() + "'" +
            ", typeOfRelationship=" + getTypeOfRelationship() +
            "}";
    }
}
