package de.marckoch.skillmatrix.skills.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.YearMonth;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private Integer projectId;

    @NotEmpty
    private String name;

    @NotNull
    private YearMonth since;

    @NotNull
    private YearMonth until;

    @AssertTrue(message = "'since' must be before 'until'")
    private boolean isSinceIsBeforeUntil() {
        if (since == null || until == null) return true; // dont validate
        return since.isBefore(until);
    }

    public boolean isNew() {
        return this.projectId == null;
    }
}
