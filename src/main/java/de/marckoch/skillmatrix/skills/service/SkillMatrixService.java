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
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SkillMatrixService {

    private final SkillRepository skillRepository;

    private final DeveloperRepository developerRepository;

    public List<Skill> buildSkillMatrix() {
        final List<Skill> skills = sortSkills(getAllSkills());

        final Set<Integer> developerIds = getDeveloperIds();

        skills.forEach(skill -> {
            addEmptyExperienceForMissingDevelopers(skill, developerIds);

            // sort all experiences again by developer weight of selected skills descending, then by name
            Comparator<Experience> weightComp = Comparator.comparing(o -> o.getDeveloper().getWeight());
            Comparator<Experience> devNameComp = Comparator.comparing(o -> o.getDeveloper().getLastName());
            skill.getExperiences().sort(weightComp.reversed().thenComparing(devNameComp));
        });

        return skills;
    }

    private void addEmptyExperienceForMissingDevelopers(Skill skill, Set<Integer> devIdsOfSkills) {
        List<Experience> experiences = skill.getExperiences();
        List<Integer> idsOfMissingDevs = findIdsOfMissingDevs(devIdsOfSkills, experiences);

        idsOfMissingDevs.forEach(developerId -> {
            Experience e = createEmptyExperienceForDeveloper(developerId);
            experiences.add(e);
        });
    }

    private List<Integer> findIdsOfMissingDevs(Set<Integer> idsOfAllDevelopers, List<Experience> experiences) {
        final List<Integer> idsOfDevelopersWithThisSkill = experiences.stream()
                .map(exp -> exp.getDeveloper().getDeveloperId())
                .toList();
        return idsOfAllDevelopers.stream()
                .filter(id -> !idsOfDevelopersWithThisSkill.contains(id))
                .toList();
    }

    private Experience createEmptyExperienceForDeveloper(Integer developerId) {
        return Experience.builder()
                .developer(developerRepository.findById(developerId).orElseThrow())
                .rating(0)
                .years(0)
                .build();
    }

    private List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    private List<Skill> sortSkills(List<Skill> skills) {
        Comparator<Skill> skillWeightComp = Comparator.comparing(HasExperiences::getWeight);
        Comparator<Skill> skillNameComp = Comparator.comparing(Skill::getName);

        return skills.stream()
                .sorted(skillWeightComp.reversed().thenComparing(skillNameComp))
                .toList();
    }

    public Set<Integer> getDeveloperIds() {
        return developerRepository.findAll()
                .stream()
                .map(Developer::getDeveloperId)
                .collect(Collectors.toSet());
    }
}
