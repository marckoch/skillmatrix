package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Experience;

import java.util.Comparator;
import java.util.List;

public class ExperienceSorter {
    public static void sortExperiences(List<Experience> experiences, String sortField, String sortDir) {
        final Comparator<Experience> byWeight = Comparator.comparing(Experience::getWeight);
        final Comparator<Experience> byRating = Comparator.comparing(Experience::getRating);
        final Comparator<Experience> byDeveloperFullName = Comparator.comparing(o -> o.getDeveloper().getFullName());
        final Comparator<Experience> bySkillName = Comparator.comparing(o -> o.getSkill().getNameAndVersion());

        switch (sortField) {
            case "skillName":
                if ("asc".equalsIgnoreCase(sortDir))
                    experiences.sort(bySkillName);
                else
                    experiences.sort(bySkillName.reversed());
                break;
            case "devFullName":
                if ("asc".equalsIgnoreCase(sortDir))
                    experiences.sort(byDeveloperFullName);
                else
                    experiences.sort(byDeveloperFullName.reversed());
                break;
            case "rating":
                if ("asc".equalsIgnoreCase(sortDir))
                    experiences.sort(byRating);
                else
                    experiences.sort(byRating.reversed());
                break;
            case "weight":
                if ("asc".equalsIgnoreCase(sortDir))
                    experiences.sort(byWeight);
                else
                    experiences.sort(byWeight.reversed());
                break;
            default:
                experiences.sort(byWeight.reversed());
        }
    }
}