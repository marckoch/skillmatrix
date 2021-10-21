package de.marckoch.skillmatrix.skills.service;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.Skill;

import java.util.List;

public class MissingDeveloperUtil {
    private MissingDeveloperUtil() {}

    // the given skill has some experiences, each of those points to a developer.
    // the given list of developers contains ALL developers.
    // this method add an empty experience for all developers that are NOT YET in skills experience list.
    //
    // example:
    // we have developers D1, D2, D3, D4
    // skill has experiences E1 (pointing to dev D3) and E2 (pointing to dev D1)
    // this method adds two empty exps pointing to D2 and D4
    // at the end skill has an experience for every developer in given list of developers
    public static void addEmptyExperienceForMissingDevelopers(Skill skill, List<Developer> developers) {
        final List<Experience> experiences = skill.getExperiences();
        final List<Developer> missingDevs = findMissingDevs(developers, experiences);

        missingDevs.forEach(developer -> {
            Experience e = Experience.createEmptyExperienceForDeveloper(developer);
            experiences.add(e);
        });
    }

    private static List<Developer> findMissingDevs(List<Developer> developers, List<Experience> experiences) {
        final List<Integer> idsOfDevelopersWithThisSkill = experiences.stream()
                .map(exp -> exp.getDeveloper().getDeveloperId())
                .toList();
        return developers.stream()
                .filter(developer -> !idsOfDevelopersWithThisSkill.contains(developer.getDeveloperId()))
                .toList();
    }
}
