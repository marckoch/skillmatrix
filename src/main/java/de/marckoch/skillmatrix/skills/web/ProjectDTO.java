package de.marckoch.skillmatrix.skills.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ProjectDatesValidation
public class ProjectDTO {

    private Integer projectId;

    @NotEmpty
    private String name;

    @NotNull
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "must match yyyy-MM, e.g. 2006-11")
    @DateTimeFormat(pattern = "yyyy-MM")
    private String since;

    @NotNull
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "must match yyyy-MM, e.g. 2006-11")
    @DateTimeFormat(pattern = "yyyy-MM")
    private String until;

    public boolean isNew() {
        return this.projectId == null;
    }
}
