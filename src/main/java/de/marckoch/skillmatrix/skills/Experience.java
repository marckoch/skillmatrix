package de.marckoch.skillmatrix.skills;

import lombok.*;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Simple JavaBean domain object representing an experience, that is a connection between a person and a skill.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
	@NotNull
	@Positive
	@Max(50)
	private Integer years;

	@Column(name = "rating")
	@NotNull
	private Integer rating;

	public boolean isNew() {
		return this.experienceid == null;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)
				.append("id", this.experienceid)
				.append("new", this.isNew())
				.append("developerId", this.developer != null ? this.developer.getDeveloperId() : "null")
				.append("skillId", this.skill != null ?  this.skill.getSkillId() : "null")
				.append("years", this.years)
				.append("rating", this.rating)
				.toString();
	}

	public int getWeight() {
		return getRating() * getYears();
	}
}
