package de.marckoch.skillmatrix.skills.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * Simple business object representing a skill.
 */
@Builder
@Getter
@Setter
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
