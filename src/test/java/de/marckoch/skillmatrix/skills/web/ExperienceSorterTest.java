package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.Skill;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.marckoch.skillmatrix.skills.web.ExperienceSorter.sortExperiences;
import static de.marckoch.skillmatrix.skills.web.SortDirection.ASC;
import static de.marckoch.skillmatrix.skills.web.SortDirection.DESC;

class ExperienceSorterTest {
    private Experience e1 = Experience.builder()
            .skill(Skill.builder().name("s1").build())
            .developer(Developer.builder().firstName("fn1").lastName("ln1").build())
            .rating(1)
            .years(10)
            .build();
    private Experience e2 = Experience.builder()
            .skill(Skill.builder().name("s2").build())
            .developer(Developer.builder().firstName("fn2").lastName("ln1").build())
            .rating(5)
            .years(10)
            .build();

    @Test
    void sortBySkillName() {
        List<Experience> experiences = new ArrayList<>(Arrays.asList(e2, e1));

        sortExperiences(experiences, "skillName", ASC);

        Assertions.assertThat(experiences).containsExactly(e1, e2);

        sortExperiences(experiences, "skillName", DESC);

        Assertions.assertThat(experiences).containsExactly(e2, e1);
    }

    @Test
    void sortByDevLastName() {
        List<Experience> experiences = new ArrayList<>(Arrays.asList(e2, e1));

        sortExperiences(experiences, "devFullName", ASC);

        Assertions.assertThat(experiences).containsExactly(e1, e2);

        sortExperiences(experiences, "devFullName", DESC);

        Assertions.assertThat(experiences).containsExactly(e2, e1);
    }

    @Test
    void sortByRating() {
        List<Experience> experiences = new ArrayList<>(Arrays.asList(e2, e1));

        sortExperiences(experiences, "rating", ASC);

        Assertions.assertThat(experiences).containsExactly(e1, e2);

        sortExperiences(experiences, "rating", DESC);

        Assertions.assertThat(experiences).containsExactly(e2, e1);
    }

    @Test
    void sortByWeight() {
        List<Experience> experiences = new ArrayList<>(Arrays.asList(e2, e1));

        sortExperiences(experiences, "weight", ASC);

        Assertions.assertThat(experiences).containsExactly(e1, e2);

        sortExperiences(experiences, "weight", DESC);

        Assertions.assertThat(experiences).containsExactly(e2, e1);
    }

    @Test
    void defautSortIsByWeightDesc() {
        List<Experience> experiences = new ArrayList<>(Arrays.asList(e1, e2));

        sortExperiences(experiences, "xxx", "");

        Assertions.assertThat(experiences).containsExactly(e2, e1);
    }
}