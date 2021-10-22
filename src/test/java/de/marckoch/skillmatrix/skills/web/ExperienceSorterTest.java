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

        sortExperiences(experiences, "skillName", "asc");

        Assertions.assertThat(experiences).containsExactly(e1, e2);

        sortExperiences(experiences, "skillName", "desc");

        Assertions.assertThat(experiences).containsExactly(e2, e1);
    }

    @Test
    void sortByDevLastName() {
        List<Experience> experiences = new ArrayList<>(Arrays.asList(e2, e1));

        sortExperiences(experiences, "devFullName", "asc");

        Assertions.assertThat(experiences).containsExactly(e1, e2);

        sortExperiences(experiences, "devFullName", "desc");

        Assertions.assertThat(experiences).containsExactly(e2, e1);
    }

    @Test
    void sortByRating() {
        List<Experience> experiences = new ArrayList<>(Arrays.asList(e2, e1));

        sortExperiences(experiences, "rating", "asc");

        Assertions.assertThat(experiences).containsExactly(e1, e2);

        sortExperiences(experiences, "rating", "desc");

        Assertions.assertThat(experiences).containsExactly(e2, e1);
    }

    @Test
    void sortByWeight() {
        List<Experience> experiences = new ArrayList<>(Arrays.asList(e2, e1));

        sortExperiences(experiences, "weight", "asc");

        Assertions.assertThat(experiences).containsExactly(e1, e2);

        sortExperiences(experiences, "weight", "desc");

        Assertions.assertThat(experiences).containsExactly(e2, e1);
    }

    @Test
    void defautSortIsByWeightDesc() {
        List<Experience> experiences = new ArrayList<>(Arrays.asList(e1, e2));

        sortExperiences(experiences, "xxx", "");

        Assertions.assertThat(experiences).containsExactly(e2, e1);
    }
}