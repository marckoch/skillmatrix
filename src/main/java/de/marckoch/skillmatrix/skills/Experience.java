package de.marckoch.skillmatrix.skills;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@Table(name = "experiences")
public class Experience {

	@Id
	@Column(name = "experience_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer experienceid;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "developer_id")
	private Developer developer;

	@EqualsAndHashCode.Exclude
	@ManyToOne
	@JoinColumn(name = "skill_id")
	private Skill skill;

	@Column(name = "years")
	private Integer years;

	@Column(name = "rating")
	private Integer rating;

}
