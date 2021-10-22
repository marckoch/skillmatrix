package de.marckoch.skillmatrix.skills.service;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Experience;
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
        final List<Skill> skills = SkillSortUtil.sortSkills(getAllSkills());

        List<Developer> developers = developerRepository.findAllForSkillMatrix();

        return buildSkillList(skills, developers);
    }

    private List<Skill> buildSkillList(List<Skill> skills, List<Developer> developers) {
        if (skills.isEmpty()) return skills;

        // sort experiences by developer weight of selected skills descending, then by name
        final Comparator<Experience> byDeveloperWeight = Comparator.comparing(o -> o.getDeveloper().getWeight());
        final Comparator<Experience> byDeveloperLastName = Comparator.comparing(o -> o.getDeveloper().getLastName());
        final Comparator<Experience> byWeightDescendingThenName = byDeveloperWeight.reversed().thenComparing(byDeveloperLastName);

        skills.forEach(skill -> {
            MissingDeveloperUtil.addEmptyExperienceForMissingDevelopers(skill, developers);
            skill.getExperiences().sort(byWeightDescendingThenName);
        });

        return skills;
    }

    private List<Skill> getAllSkills() {
        return skillRepository.findAllForSkillMatrix();
    }
}
