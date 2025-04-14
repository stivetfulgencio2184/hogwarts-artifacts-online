package org.alpha.omega.hogwarts_artifacts_online.artifact.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "Wizard")
@Table(name = "Wizards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wizard  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @OneToMany(mappedBy = "wizard", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<Artifact> artifacts = new HashSet<>();


    public void addArtifact(Artifact artifact) {
        artifact.setWizard(this);
        this.artifacts.add(artifact);
    }

    public void removeArtifact(Artifact artifact) {
        this.artifacts.remove(artifact);
        artifact.setWizard(null);
    }

    public Integer getNumberOfArtifacts() {
        return this.getArtifacts().size();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Wizard other = (Wizard) obj;
        if (id == null) {
            return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Wizard{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
