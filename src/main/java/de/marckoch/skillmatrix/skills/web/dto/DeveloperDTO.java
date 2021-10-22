package de.marckoch.skillmatrix.skills.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperDTO {

    private Integer developerId;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    private String title;

    public boolean isNew() {
        return this.developerId == null;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}
