package de.marckoch.skillmatrix.skills.service;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillsServiceTest {

    @InjectMocks
    SkillsService skillsService;

    @Mock
    SkillRepository skillRepository;

    Skill skill1 = Skill.builder().skillId(1).name("skill1").build();
    Skill skill2 = Skill.builder().skillId(2).name("skill2").build();
    Skill skill3 = Skill.builder().skillId(3).name("skill3").build();
    Developer dev1 = Developer.builder().developerId(123).build();

    @BeforeEach
    void setup() {
        when(skillRepository.findAllForFreeSkills()).thenReturn(List.of(skill1, skill2, skill3));
    }

    @Test
    void freeSkills() {
        dev1.setExperiences(List.of(Experience.builder().skill(skill1).build()));

        List<Skill> freeSkills = skillsService.getFreeSkills(dev1);

        assertThat(freeSkills).extracting(Skill::getSkillId).containsExactly(2, 3);
        assertThat(freeSkills).extracting(Skill::getName).containsExactly("skill2", "skill3");
    }

    @Test
    void freeSkillsHasAllSkillsWhenDeveloperHasNoSkills() {
        // dev gets no experiences!

        List<Skill> freeSkills = skillsService.getFreeSkills(dev1);

        assertThat(freeSkills).extracting(Skill::getSkillId).containsExactly(1, 2, 3);
        assertThat(freeSkills).extracting(Skill::getName).containsExactly("skill1", "skill2", "skill3");
    }

    @Test
    void freeSkillsIsEmptyWhenDeveloperHasAllSkills() {
        dev1.setExperiences(List.of(
                Experience.builder().skill(skill1).build(),
                Experience.builder().skill(skill2).build(),
                Experience.builder().skill(skill3).build()));

        List<Skill> freeSkills = skillsService.getFreeSkills(dev1);

        assertThat(freeSkills).isEmpty();
    }
}