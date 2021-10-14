package de.marckoch.skillmatrix.skills.service;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.random.RandomGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class SkillMatrixServiceTest {

    @Autowired
    SkillMatrixService skillMatrixService;

    @MockBean
    SkillRepository skillRepository;

    @MockBean
    DeveloperRepository developerRepository;

    private final List<Developer> developers = new ArrayList<>();
    private final List<Skill> skills = new ArrayList<>();
    private final List<Experience> experiences = new ArrayList<>();

    @Test
    void skillMatrixWorksWithBigData() {
        when(skillRepository.findAll()).thenReturn(skills);
        when(developerRepository.findAll()).thenReturn(developers);
        developers.forEach(developer ->
                when(developerRepository.findById(developer.getDeveloperId())).thenReturn(Optional.of(developer)));

        List<Skill> skills = skillMatrixService.buildSkillMatrix();

        assertThat(skills).hasSize(100);
        skills.forEach(skill -> {
            assertThat(skill.getExperiences()).hasSize(100);
            // TODO: assert each row has same order of developers!
        });
    }

    @BeforeEach
    public void createData() {
        for (int d = 0; d < 100; d++) {
            Developer dev = new Developer();
            dev.setDeveloperId(d);
            dev.setFirstName("firstName" + d);
            dev.setLastName("lastName" + d);
            dev.setExperiences(new ArrayList<>());
            developers.add(dev);

            Skill skill = new Skill();
            skill.setSkillId(d);
            skill.setName("skill" + d);
            skill.setExperiences(new ArrayList<>());
            skills.add(skill);
        }

        RandomGenerator randomGenerator = RandomGenerator.getDefault();

        int countExp = 0;
        while (countExp < 200) {
            Developer d = developers.get(randomGenerator.nextInt(developers.size()));
            Skill skill = skills.get(randomGenerator.nextInt(skills.size()));

            Experience exp = new Experience();
            exp.setExperienceid(countExp);
            exp.setRating(randomGenerator.nextInt(5) + 1);
            exp.setYears(randomGenerator.nextInt(20) + 1);

            if (d.getExperiences().isEmpty() && skill.getExperiences().isEmpty()) {
                exp.setDeveloper(d);
                d.getExperiences().add(exp);
                exp.setSkill(skill);
                skill.getExperiences().add(exp);
                experiences.add(exp);
            }
            countExp++;
        }
        System.out.println("data loaded");
    }
}