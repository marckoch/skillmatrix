package de.marckoch.skillmatrix.skills;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class SkillsServiceTest {

    @Autowired
    SkillsService skillsService;

    @MockBean
    SkillRepository skillRepository;

    Skill skill1 = Skill.builder().skillId(1).name("skill1").build();
    Skill skill2 = Skill.builder().skillId(2).name("skill2").build();
    Skill skill3 = Skill.builder().skillId(3).name("skill3").build();
    Developer dev1 = Developer.builder().developerId(123).build();

    @BeforeEach
    void setup() {
        when(skillRepository.findAll()).thenReturn(List.of(skill1, skill2, skill3));
    }

    @Test
    void getFreeSkills() {
        dev1.setExperiences(List.of(Experience.builder().skill(skill1).build()));

        List<SelectItem> freeSkills = skillsService.getFreeSkills(dev1);

        assertThat(freeSkills).extracting(selectItem -> selectItem.key).containsExactly(2, 3);
        assertThat(freeSkills).extracting(selectItem -> selectItem.value).containsExactly("skill2", "skill3");
    }

    @Test
    void getFreeSkillsWhenDeveloperHasNoSkills() {
        // dev gets no experiences!

        List<SelectItem> freeSkills = skillsService.getFreeSkills(dev1);

        assertThat(freeSkills).extracting(selectItem -> selectItem.key).containsExactly(1, 2, 3);
        assertThat(freeSkills).extracting(selectItem -> selectItem.value).containsExactly("skill1", "skill2", "skill3");
    }

    @Test
    void getFreeSkillsWhenDeveloperHasAllSkills() {
        dev1.setExperiences(List.of(
                Experience.builder().skill(skill1).build(),
                Experience.builder().skill(skill2).build(),
                Experience.builder().skill(skill3).build()));

        List<SelectItem> freeSkills = skillsService.getFreeSkills(dev1);

        assertThat(freeSkills).isEmpty();
    }
}