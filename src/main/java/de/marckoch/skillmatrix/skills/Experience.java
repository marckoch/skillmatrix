package de.marckoch.skillmatrix.skills;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Simple JavaBean domain object representing an experience, that is a connection between a person and a skill.
 */
@Entity
@Data
@Table(name = "experiences")
public class Experience {

	@Id
	@Column(name = "experience_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer experienceid;

	@EqualsAndHashCode.Exclude
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "developer_id")
	private Developer developer;

	@EqualsAndHashCode.Exclude
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "skill_id")
	private Skill skill;

	@Column(name = "years")
	private Integer years;

	@Column(name = "rating")
	private Integer rating;

	@Override
	public String toString() {
		return "Experience{" +
				"experienceid=" + experienceid +
				", developer=" + developer.getFirstName() + " " + developer.getLastName() +
				", skill=" + skill.getName() + " " + skill.getVersion() +
				", years=" + years +
				", rating=" + rating +
				'}';
	}
}
