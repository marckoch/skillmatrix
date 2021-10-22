package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.Skill;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.marckoch.skillmatrix.skills.web.ExperienceSorter.sortExperiences;
import static de.marckoch.skillmatrix.skills.web.SortDirection.ASC;
import static de.marckoch.skillmatrix.skills.web.SortDirection.DESC;

class ExperienceSorterTest {
    private final Experience e1 = Experience.builder()
            .skill(Skill.builder().name("s1").build())
            .developer(Developer.builder().firstName("fn1").lastName("ln1").build())
            .rating(1)
            .years(10)
            .build();
    private final Experience e2 = Experience.builder()
            .skill(Skill.builder().name("s2").build())
            .developer(Developer.builder().firstName("fn2").lastName("ln1").build())
            .rating(5)
            .years(10)
            .build();

    @ParameterizedTest
    @ValueSource(strings = {"skillName", "devFullName", "rating", "weight"})
    void sort(String sortField) {
        List<Experience> experiences = new ArrayList<>(Arrays.asList(e2, e1));

        sortExperiences(experiences, sortField, ASC);

        Assertions.assertThat(experiences).containsExactly(e1, e2);

        sortExperiences(experiences, sortField, DESC);

        Assertions.assertThat(experiences).containsExactly(e2, e1);
    }

    @Test
    void defaultSortIsByWeightDesc() {
        List<Experience> experiences = new ArrayList<>(Arrays.asList(e1, e2));

        sortExperiences(experiences, "xxx", "");

        Assertions.assertThat(experiences).containsExactly(e2, e1);
    }
}