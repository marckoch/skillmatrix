package de.marckoch.skillmatrix.skills.service;

import de.marckoch.skillmatrix.skills.entity.HasExperiences;
import de.marckoch.skillmatrix.skills.entity.Skill;

import java.util.Comparator;
import java.util.List;

public class SkillSortUtil {
    private SkillSortUtil() {}

    public static List<Skill> sortSkills(List<Skill> skills) {
        final Comparator<Skill> bySkillWeight = Comparator.comparing(HasExperiences::getWeight);
        final Comparator<Skill> bySkillName = Comparator.comparing(Skill::getName);

        return skills.stream()
                .sorted(bySkillWeight.reversed().thenComparing(bySkillName))
                .toList();
    }
}
