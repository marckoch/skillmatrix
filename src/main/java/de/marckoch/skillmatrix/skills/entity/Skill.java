package de.marckoch.skillmatrix.skills.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Comparator;
import java.util.List;

/**
 * Simple business object representing a skill.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "skills")
public class Skill implements HasExperiences {

	@Id
	@Column(name = "skill_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer skillId;

	@NotEmpty
	@Column(name = "name")
	private String name;

	@Column(name = "version")
	private String version;

	@Column(name = "alias")
	private String alias;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy = "skill", fetch = FetchType.EAGER)
	private List<Experience> experiences;

	public boolean isNew() {
		return this.skillId == null;
	}

	public List<Experience> topThreeExperts() {
		return experiences.stream()
				.sorted(Comparator.comparing(Experience::getRating).reversed())
				.limit(3)
				.toList();
	}

	public String getNameAndVersion() {
		return getName() + (getVersion() != null ? " " + getVersion() : "");
	}
}
