package de.marckoch.skillmatrix.skills.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.YearMonth;

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
    private YearMonth since;

    @NotNull
    private YearMonth until;

    public boolean isNew() {
        return this.projectId == null;
    }
}
