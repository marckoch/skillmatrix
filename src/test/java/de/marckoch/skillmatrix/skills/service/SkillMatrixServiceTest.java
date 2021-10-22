package de.marckoch.skillmatrix.skills.service;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.HasExperiences;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.random.RandomGenerator;

import static java.util.Comparator.comparingInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillMatrixServiceTest {

    @InjectMocks
    SkillMatrixService skillMatrixService;

    @Mock
    SkillRepository skillRepository;

    @Mock
    DeveloperRepository developerRepository;

    final int NUMBER_OF_DEVELOPERS = 100;
    final int NUMBER_OF_SKILLS = 200;
    final int NUMBER_OF_EXPERIENCES = NUMBER_OF_DEVELOPERS * NUMBER_OF_SKILLS / 2;

    private final List<Developer> developers = new ArrayList<>();
    private final List<Skill> skills = new ArrayList<>();
    private final List<Experience> experiences = new ArrayList<>();

    @Test
    void skillMatrixWorksWithEmptyInput() {
        when(skillRepository.findAll()).thenReturn(Collections.emptyList());

        final List<Skill> skills = skillMatrixService.getSkillsForSkillMatrix();

        assertThat(skills).isEmpty();
    }

    @Test
    void skillMatrixWorksWithBigData() {
        // given
        createRandomTestData();

        when(skillRepository.findAll()).thenReturn(skills);
        when(developerRepository.findAll()).thenReturn(developers);

        // when
        final List<Skill> skills = skillMatrixService.getSkillsForSkillMatrix();

        // then
        assertThat(skills).hasSize(NUMBER_OF_SKILLS);
        assertThat(skills).map(HasExperiences::getWeight)
                .isSortedAccordingTo(comparingInt(o -> (int) o).reversed());

        final List<Integer> sortedDeveloperIdsOfFirstSkill = skills.get(0).getDeveloperIds();

        skills.forEach(skill -> {
            assertThat(skill.getExperiences()).hasSize(NUMBER_OF_DEVELOPERS);
            assertThat(skill.getDeveloperIds()).isEqualTo(sortedDeveloperIdsOfFirstSkill);
        });
    }

    public void createRandomTestData() {
        for (int d = 0; d < NUMBER_OF_DEVELOPERS; d++) {
            Developer dev = new Developer();
            dev.setDeveloperId(d);
            dev.setFirstName("firstName" + d);
            dev.setLastName("lastName" + d);
            dev.setExperiences(new ArrayList<>());
            developers.add(dev);
        }

        for (int s = 0; s < NUMBER_OF_SKILLS; s++) {
            Skill skill = new Skill();
            skill.setSkillId(s);
            skill.setName("skill" + s);
            skill.setExperiences(new ArrayList<>());
            skills.add(skill);
        }

        RandomGenerator randomGenerator = RandomGenerator.getDefault();

        int countExp = 0;
        while (experiences.size() < NUMBER_OF_EXPERIENCES) {
            Developer d = developers.get(randomGenerator.nextInt(developers.size()));

            Skill skill = skills.get(randomGenerator.nextInt(skills.size()));
            if (d.getSkillIds().contains(skill.getSkillId())) {
                continue;
            }

            Experience exp = new Experience();
            exp.setExperienceId(countExp);
            exp.setRating(randomGenerator.nextInt(5) + 1);
            exp.setYears(randomGenerator.nextInt(20) + 1);

            exp.setDeveloper(d);
            d.getExperiences().add(exp);
            exp.setSkill(skill);
            skill.getExperiences().add(exp);
            experiences.add(exp);

            countExp++;
        }

        System.out.println("data loaded:");
        System.out.println("  " + developers.size() + " developers");
        System.out.println("  " + skills.size() + " skills");
        System.out.println("  " + experiences.size() + " experiences");
    }
}