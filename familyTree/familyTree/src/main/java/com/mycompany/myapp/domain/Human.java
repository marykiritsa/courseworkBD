package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Human.
 */
@Entity
@Table(name = "human")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Human implements Serializable {

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
    @Column(name = "human_info", nullable = false)
    private Integer humanInfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Human id(Long id) {
        this.id = id;
        return this;
    }

    public String getSurname() {
        return this.surname;
    }

    public Human surname(String surname) {
        this.surname = surname;
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return this.name;
    }

    public Human name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return this.patronymic;
    }

    public Human patronymic(String patronymic) {
        this.patronymic = patronymic;
        return this;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Integer getHumanInfo() {
        return this.humanInfo;
    }

    public Human humanInfo(Integer humanInfo) {
        this.humanInfo = humanInfo;
        return this;
    }

    public void setHumanInfo(Integer humanInfo) {
        this.humanInfo = humanInfo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Human)) {
            return false;
        }
        return id != null && id.equals(((Human) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Human{" +
            "id=" + getId() +
            ", surname='" + getSurname() + "'" +
            ", name='" + getName() + "'" +
            ", patronymic='" + getPatronymic() + "'" +
            ", humanInfo=" + getHumanInfo() +
            "}";
    }
}
