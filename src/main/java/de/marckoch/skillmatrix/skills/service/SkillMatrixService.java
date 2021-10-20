package de.marckoch.skillmatrix.skills.service;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.HasExperiences;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Service
public class SkillMatrixService {

    private final SkillRepository skillRepository;

    private final DeveloperRepository developerRepository;

    public List<Skill> getSkillsForSkillMatrix() {
        final List<Skill> skills = sortSkills(getAllSkills());

        List<Developer> developers = developerRepository.findAll();

        return buildSkillList(skills, developers);
    }

    private List<Skill> buildSkillList(List<Skill> skills, List<Developer> developers) {
        skills.forEach(skill -> {
            addEmptyExperienceForMissingDevelopers(skill, developers);

            // sort all experiences again by developer weight of selected skills descending, then by name
            Comparator<Experience> weightComp = Comparator.comparing(o -> o.getDeveloper().getWeight());
            Comparator<Experience> devNameComp = Comparator.comparing(o -> o.getDeveloper().getLastName());
            skill.getExperiences().sort(weightComp.reversed().thenComparing(devNameComp));
        });

        return skills;
    }

    private List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    private void addEmptyExperienceForMissingDevelopers(Skill skill, List<Developer> developers) {
        List<Experience> experiences = skill.getExperiences();
        List<Developer> missingDevs = findMissingDevs(developers, experiences);

        missingDevs.forEach(developer -> {
            Experience e = createEmptyExperienceForDeveloper(developer);
            experiences.add(e);
        });
    }

    private List<Developer> findMissingDevs(List<Developer> developers, List<Experience> experiences) {
        final List<Integer> idsOfDevelopersWithThisSkill = experiences.stream()
                .map(exp -> exp.getDeveloper().getDeveloperId())
                .toList();
        return developers.stream()
                .filter(developer -> !idsOfDevelopersWithThisSkill.contains(developer.getDeveloperId()))
                .toList();
    }

    private Experience createEmptyExperienceForDeveloper(Developer developer) {
        return Experience.builder()
                .developer(developer)
                .rating(0)
                .years(0)
                .build();
    }

    private List<Skill> sortSkills(List<Skill> skills) {
        Comparator<Skill> skillWeightComp = Comparator.comparing(HasExperiences::getWeight);
        Comparator<Skill> skillNameComp = Comparator.comparing(Skill::getName);

        return skills.stream()
                .sorted(skillWeightComp.reversed().thenComparing(skillNameComp))
                .toList();
    }
}
