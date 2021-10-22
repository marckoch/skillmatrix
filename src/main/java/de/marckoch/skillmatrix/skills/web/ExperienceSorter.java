package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Experience;

import java.util.Comparator;
import java.util.List;

import static de.marckoch.skillmatrix.skills.web.SortDirection.ASC;

/**
 * here we sort a list of experiences which usually hangs at a developer or skill instance.
 * we sort here after loading, because a flexible sorting of child instances in the db query is not possible
 */
public class ExperienceSorter {
    private ExperienceSorter() {}

    public static void sortExperiences(List<Experience> experiences, String sortField, String sortDir) {
        final Comparator<Experience> byWeight = Comparator.comparing(Experience::getWeight);
        final Comparator<Experience> byRating = Comparator.comparing(Experience::getRating);
        final Comparator<Experience> byDeveloperFullName = Comparator.comparing(o -> o.getDeveloper().getFullName());
        final Comparator<Experience> bySkillName = Comparator.comparing(o -> o.getSkill().getNameAndVersion());

        switch (sortField) {
            case "skillName":
                if (ASC.equalsIgnoreCase(sortDir))
                    experiences.sort(bySkillName);
                else
                    experiences.sort(bySkillName.reversed());
                break;
            case "devFullName":
                if (ASC.equalsIgnoreCase(sortDir))
                    experiences.sort(byDeveloperFullName);
                else
                    experiences.sort(byDeveloperFullName.reversed());
                break;
            case "rating":
                if (ASC.equalsIgnoreCase(sortDir))
                    experiences.sort(byRating);
                else
                    experiences.sort(byRating.reversed());
                break;
            case "weight":
                if (ASC.equalsIgnoreCase(sortDir))
                    experiences.sort(byWeight);
                else
                    experiences.sort(byWeight.reversed());
                break;
            default:
                experiences.sort(byWeight.reversed());
        }
    }
}