package de.marckoch.skillmatrix.skills.service;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SkillSetsService {

    private final SkillRepository skillRepository;

    private final DeveloperRepository developerRepository;

    public List<Skill> getSkillsForSkillSets(String skillSetQuery) {
        final List<Skill> skills = SkillSortUtil.sortSkills(getSkills(skillSetQuery));

        final Set<Integer> developerIds = getDeveloperIds(skills);
        List<Developer> developers = developerRepository.findAllById(developerIds);

        return buildSkillList(skills, developers);
    }

    private List<Skill> buildSkillList(List<Skill> skills, List<Developer> developers) {
        if (skills.isEmpty()) return skills;

        // sort experiences by developer weight of selected skills descending, then by name
        // problem: this is slow! when we sort all experiences of all skills by this, it is taking too long!
        final Comparator<Experience> byDevWeightForTheseSkills = Comparator.comparing(o -> o.getDeveloper().getWeightForSkills(skills));
        final Comparator<Experience> byDeveloperLastName = Comparator.comparing(o -> o.getDeveloper().getLastName());
        final Comparator<Experience> byWeightDescendingThenName = byDevWeightForTheseSkills.reversed().thenComparing(byDeveloperLastName);

        // sort first skill experiences by slow weightComp
        MissingDeveloperUtil.addEmptyExperienceForMissingDevelopers(skills.get(0), developers);
        skills.get(0).getExperiences().sort(byWeightDescendingThenName);
        final List<Integer> developerIdsOfFirstSkillSortedByWeight = skills.get(0).getDeveloperIds();

        // build new Comp based on the order of experiences in the first skill (that was sorted with the old, slow weight comp)
        final Comparator<Experience> byOrderOfDevsInFirstSkill = Comparator.comparingInt(e ->
                developerIdsOfFirstSkillSortedByWeight.indexOf(e.getDeveloper().getDeveloperId()));

        // now sort all skill experiences (incl first one again) by new fast orderComp
        skills.forEach(skill -> {
            MissingDeveloperUtil.addEmptyExperienceForMissingDevelopers(skill, developers);
            skill.getExperiences().sort(byOrderOfDevsInFirstSkill);
        });

        return skills;
    }

    private List<Skill> getSkills(String query) {
        if (query == null || query.isEmpty())
            return Collections.emptyList();

        // this should/could be done in one query, but for now this is good enough.
        // user will enter only a handful of search terms, so we do a query for each of them.
        if (query.contains(",")) {
            List<String> searchTerms = Arrays.stream(query.split(",")).map(s -> s.trim().toUpperCase()).toList();
            return searchTerms.stream()
                    .map(skillRepository::findByQuery)
                    .flatMap(Collection::stream)
                    .distinct()
                    .toList();
        } else
            return skillRepository.findByQuery(query.toUpperCase());
    }

    private Set<Integer> getDeveloperIds(List<Skill> skills) {
        return skills.stream()
                .flatMap(s -> s.getDeveloperIds().stream())
                .collect(Collectors.toSet());
    }
}
