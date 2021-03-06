package io.github.robertovillarejo.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import io.github.robertovillarejo.domain.enumeration.SizeModel;

/**
 * A Mold.
 */
@Entity
@Table(name = "mold")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Mold implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "number", nullable = false)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "size")
    private SizeModel size;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "mold_models",
               joinColumns = @JoinColumn(name = "mold_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "models_id", referencedColumnName = "id"))
    private Set<Model> models = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public Mold number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public SizeModel getSize() {
        return size;
    }

    public Mold size(SizeModel size) {
        this.size = size;
        return this;
    }

    public void setSize(SizeModel size) {
        this.size = size;
    }

    public Set<Model> getModels() {
        return models;
    }

    public Mold models(Set<Model> models) {
        this.models = models;
        return this;
    }

    public Mold addModels(Model model) {
        this.models.add(model);
        model.getMolds().add(this);
        return this;
    }

    public Mold removeModels(Model model) {
        this.models.remove(model);
        model.getMolds().remove(this);
        return this;
    }

    public void setModels(Set<Model> models) {
        this.models = models;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mold)) {
            return false;
        }
        return id != null && id.equals(((Mold) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Mold{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", size='" + getSize() + "'" +
            "}";
    }
}
