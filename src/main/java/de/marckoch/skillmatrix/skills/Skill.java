package de.marckoch.skillmatrix.skills;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Simple business object representing a skill.
 */
@Entity
@Data
@Table(name = "skills")
public class Skill {

	@Id
	@Column(name = "skill_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer skillId;

	@Column(name = "name")
	private String name;

	@Column(name = "version")
	private String version;

	@Column(name = "alias")
	private String alias;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy = "skill")
	private Set<Experience> experiences;

	public List<Experience> topThreeExperts() {
		return experiences.stream()
				.sorted(Comparator.comparing(Experience::getRating).reversed())
				.limit(3)
				.toList();
	}
}
