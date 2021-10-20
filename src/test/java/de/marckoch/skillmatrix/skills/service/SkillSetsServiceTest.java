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
import java.util.Set;
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

    final int NUMBER_OF_DEVELOPERS = 10;
    final int NUMBER_OF_SKILLS = 200;
    final int NUMBER_OF_EXPERIENCES = 100;

    private final List<Developer> developers = new ArrayList<>();
    private final List<Skill> skills = new ArrayList<>();
    private final List<Experience> experiences = new ArrayList<>();

    @Test
    void skillSetsWorksWithEmptyInput() {
        final List<Skill> skills = skillSetsService.getSkillsForSkillSets("");

        assertThat(skills).isEmpty();
    }

    @Test
    void skillSetsWorksWithBigData() {
        createRandomTestData();

        List<Experience> randomExp = getRandomExperiences(5);
        List<Skill> searchSkills = randomExp.stream().map(Experience::getSkill).distinct().toList();

        searchSkills.forEach(skill ->
                when(skillRepository.findByQuery(skill.getName().toUpperCase())).thenReturn(List.of(skill)));

        Set<Integer> developerIdsOfSearchedSkills = getDeveloperIds(searchSkills);
        List<Developer> developersOfSearchedSkills = developers.stream().filter(developer -> developerIdsOfSearchedSkills.contains(developer.getDeveloperId())).toList();
        when(developerRepository.findAllById(developerIdsOfSearchedSkills)).thenReturn(developersOfSearchedSkills);

        // when
        final List<Skill> skills = skillSetsService.getSkillsForSkillSets(commaSeperatedListOfUppercaseSkillNames(searchSkills));

        // then
        assertThat(skills).hasSize(searchSkills.size());
        assertThat(skills).map(HasExperiences::getWeight)
                .isSortedAccordingTo(comparingInt(o -> (int) o).reversed());

        final List<Integer> sortedDeveloperIdsOfFirstSkill = skills.get(0).getDeveloperIds();

        skills.forEach(skill -> {
            assertThat(skill.getExperiences()).hasSize(developersOfSearchedSkills.size());
            assertThat(skill.getDeveloperIds()).isEqualTo(sortedDeveloperIdsOfFirstSkill);
        });
    }

    private String commaSeperatedListOfUppercaseSkillNames(List<Skill> skills) {
        return skills.stream()
                .map(skill -> skill.getName().toUpperCase())
                .collect(Collectors.joining(","));
    }

    private Set<Integer> getDeveloperIds(List<Skill> skills) {
        return skills.stream()
                .flatMap(s -> s.getDeveloperIds().stream())
                .collect(Collectors.toSet());
    }

    private List<Experience> getRandomExperiences(int count) {
        RandomGenerator randomGenerator = RandomGenerator.getDefault();
        List<Experience> randomExps = new ArrayList<>();
        while (randomExps.size() < count) {
            Experience exp = experiences.get(randomGenerator.nextInt(experiences.size()));
            if (!randomExps.contains(exp)) {
                randomExps.add(exp);
            }
        }
        return randomExps;
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