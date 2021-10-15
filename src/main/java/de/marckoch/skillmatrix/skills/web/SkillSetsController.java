package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.HasExperiences;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
class SkillSetsController {

    private final SkillRepository skillRepository;

    private final DeveloperRepository developerRepository;

    @GetMapping("/skills/sets")
    public String skillSets(@RequestParam(required = false) String skillSetQuery, Model model) {
        final List<Skill> skills = sortSkills(getSkills(skillSetQuery));

        final Set<Integer> developerIds = getDeveloperIds(skills);

        skills.forEach(skill -> {
            addEmptyExperienceForMissingDevelopers(skill, developerIds);

            // sort all experiences again by developer weight of selected skills descending, then by name
            Comparator<Experience> weightComp = Comparator.comparing(o -> o.getDeveloper().getWeight());
            Comparator<Experience> devNameComp = Comparator.comparing(o -> o.getDeveloper().getLastName());
            skill.getExperiences().sort(weightComp.reversed().thenComparing(devNameComp));
        });

        model.addAttribute("skills", skills);
        model.addAttribute("skillSetQuery", buildJsonOfSkillSetQuery(skillSetQuery));
        return "/skills/skillSets";
    }

    // return search terms so token input can show them again (via its prePopulate mechanism)
    private List<Map<String, String>> buildJsonOfSkillSetQuery(String skillSetQuery) {
        if (skillSetQuery==null || skillSetQuery.isEmpty()) return Collections.emptyList();
        return Arrays.stream(skillSetQuery.split(",")).map(this::toMap).toList();
    }

    private Map<String, String> toMap(String s) {
        return Map.of("id", s, "name", s);
    }

    private void addEmptyExperienceForMissingDevelopers(Skill skill, Set<Integer> devIdsOfSkills) {
        List<Experience> experiences = skill.getExperiences();
        List<Integer> idsOfMissingDevs = findIdsOfMissingDevs(devIdsOfSkills, experiences);

        idsOfMissingDevs.forEach(devId -> {
            Experience e = createEmptyExperienceForDeveloper(devId);
            experiences.add(e);
        });
    }

    private List<Integer> findIdsOfMissingDevs(Set<Integer> idsOfDevelopers, List<Experience> experiences) {
        final List<Integer> idsOfDevelopersWithThisSkill = experiences.stream()
                .map(exp -> exp.getDeveloper().getDeveloperId())
                .toList();
        return idsOfDevelopers.stream()
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

    private List<Skill> getSkills(String query) {
        if (query == null)
            return skillRepository.findAll();

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

    private List<Skill> sortSkills(List<Skill> skills) {
        Comparator<Skill> skillWeightComp = Comparator.comparing(HasExperiences::getWeight);
        Comparator<Skill> skillNameComp = Comparator.comparing(Skill::getName);

        return skills.stream()
                .sorted(skillWeightComp.reversed().thenComparing(skillNameComp))
                .toList();
    }

    public Set<Integer> getDeveloperIds(List<Skill> skills) {
        return skills.stream()
                .flatMap(s -> s.getDeveloperIds().stream())
                .collect(Collectors.toSet());
    }
}
