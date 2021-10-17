package de.marckoch.skillmatrix.skills.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * Simple business object representing a skill.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillDTO {

	private Integer skillId;

	@NotEmpty
	private String name;

	private String version;

	private String alias;

	public boolean isNew() {
		return this.skillId == null;
	}

	public String getNameAndVersion() {
		return getName() + (getVersion() != null ? " " + getVersion() : "");
	}
}
