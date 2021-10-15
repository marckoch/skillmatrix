package de.marckoch.skillmatrix.skills.service;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.HasExperiences;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class SkillSetsServiceTest {

    @Autowired
    SkillSetsService skillSetsService;

    @MockBean
    SkillRepository skillRepository;

    @MockBean
    DeveloperRepository developerRepository;

    final int NUMBER_OF_DEVELOPERS = 100;
    final int NUMBER_OF_SKILLS = 200;
    final int NUMBER_OF_EXPERIENCES = 1000;

    private final List<Developer> developers = new ArrayList<>();
    private final List<Skill> skills = new ArrayList<>();
    private final List<Experience> experiences = new ArrayList<>();

    @Test
    void skillSetsWorksWithEmptyInput() {
        final List<Skill> skills = skillSetsService.buildSkillSets("");

        assertThat(skills).isEmpty();
    }

    @Test
    void skillSetsWorksWithBigData() {
        createRandomTestData();

        List<Skill> searchSkills = getRandomSkills();

        when(skillRepository.findAll()).thenReturn(skills);
        searchSkills.forEach(skill ->
                when(skillRepository.findByQuery(skill.getName().toUpperCase())).thenReturn(List.of(skill)));

        when(developerRepository.findAll()).thenReturn(developers);
        developers.forEach(developer ->
                when(developerRepository.findById(developer.getDeveloperId())).thenReturn(Optional.of(developer)));

        // when
        final List<Skill> skills = skillSetsService.buildSkillSets(commaSeperatedListOfUppercaseSkillNames(searchSkills));

        // then
        assertThat(skills).hasSize(searchSkills.size());
        assertThat(skills).map(HasExperiences::getWeight)
                .isSortedAccordingTo(comparingInt(o -> (int) o).reversed());

        final List<Integer> sortedDeveloperIdsOfFirstSkill = skills.get(0).getDeveloperIds();

        skills.forEach(skill -> {
            assertThat(skill.getDeveloperIds()).isEqualTo(sortedDeveloperIdsOfFirstSkill);
        });
    }

    private String commaSeperatedListOfUppercaseSkillNames(List<Skill> skills) {
        return skills.stream()
                .map(skill -> skill.getName().toUpperCase())
                .collect(Collectors.joining(","));
    }

    private List<Skill> getRandomSkills() {
        RandomGenerator randomGenerator = RandomGenerator.getDefault();
        List<Skill> searchSkills = new ArrayList<>();
        while (searchSkills.size() < 5) {
            Skill skill = skills.get(randomGenerator.nextInt(skills.size()));
            if (!searchSkills.contains(skill)) {
                searchSkills.add(skill);
            }
        }
        return searchSkills;
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