package de.marckoch.skillmatrix.skills;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Simple JavaBean domain object representing someone that has skills.
 */
@Entity
@Data
@Table(name = "developers")
public class Developer {

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

	@EqualsAndHashCode.Exclude
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "developer", fetch = FetchType.EAGER, orphanRemoval = true)
	@OrderBy("rating DESC")
	private Set<Experience> experiences;

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
				.append("experiences", this.experiences)
				.toString();
	}

}
