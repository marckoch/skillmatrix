package de.marckoch.skillmatrix.skills.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Simple JavaBean domain object representing someone that has skills.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "developers")
public class Developer implements HasExperiences {

    @Id
    @Column(name = "developer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer developerId;

    @Column(name = "first_name")
    @NotEmpty
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty
    private String lastName;

    @Column(name = "title")
    private String title;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "developers_to_projects",
            joinColumns = {@JoinColumn(name = "developer_id", referencedColumnName = "developer_id")},
            inverseJoinColumns = {@JoinColumn(name = "project_id", referencedColumnName = "project_id")})
    private Project currentProject;

    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "developer", fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("rating DESC")
    private List<Experience> experiences;

    public List<Experience> topThreeExperiences() {
        return experiences.stream()
                .sorted(Comparator.comparing(Experience::getRating).reversed())
                .limit(3)
                .toList();
    }

    public boolean isNew() {
        return this.developerId == null;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("id", this.developerId)
                .append("new", this.isNew())
                .append("lastName", this.lastName)
                .append("firstName", this.firstName)
                .append("title", this.title)
//				.append("experiences", this.experiences)
                .toString();
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public Integer getWeightForSkills(List<Skill> skills) {
        return getExperiences()
                .stream()
                .filter(experience -> skills.contains(experience.getSkill()))
                .mapToInt(Experience::getWeight)
                .sum();
    }

    public List<Integer> getSkillIds() {
        if (getExperiences() == null || getExperiences().isEmpty())
            return Collections.emptyList();
        return getExperiences().stream()
                .map(exp -> exp.getSkill().getSkillId())
                .toList();
    }
}
