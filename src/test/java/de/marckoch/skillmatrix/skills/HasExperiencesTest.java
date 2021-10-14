package de.marckoch.skillmatrix.skills;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class HasExperiencesTest {

    @Test
    void emptyExperiencesReturnWeight0() {
        var x = new HasExperiences() {
            @Override
            public List<Experience> getExperiences() {
                return Collections.emptyList();
            }
        };

        Assertions.assertThat(x.getWeight()).isZero();
    }

    @Test
    void nonEmptyExperiencesReturnCorrectWeight() {
        var x = new HasExperiences() {
            final Experience exp1 = Experience.builder().years(2).rating(3).build();
            final Experience exp2 = Experience.builder().years(10).rating(5).build();

            @Override
            public List<Experience> getExperiences() {
                return List.of(exp1, exp2);
            }
        };

        Assertions.assertThat(x.getWeight()).isEqualTo(56); // 2*3 + 10*5
    }
}