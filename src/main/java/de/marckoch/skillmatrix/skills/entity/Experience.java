package de.marckoch.skillmatrix.skills.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
	private Integer experienceId;

	@EqualsAndHashCode.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "developer_id")
	private Developer developer;

	@EqualsAndHashCode.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "skill_id")
	private Skill skill;

	@Column(name = "years")
	@NotNull
	@Positive
	@Max(50)
	private Integer years;

	@Column(name = "rating")
	@NotNull
	@Range(min = 1, max = 5)
	private Integer rating;

	public boolean isNew() {
		return this.experienceId == null;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)
				.append("id", this.experienceId)
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
