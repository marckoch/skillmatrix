package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.service.SkillSetsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILLS;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILL_SET_QUERY;
import static de.marckoch.skillmatrix.skills.web.ViewNames.SKILL_SETS;

@Controller
@AllArgsConstructor
class SkillSetsController {

    private final SkillSetsService skillSetsService;

    @GetMapping("/skills/sets")
    public String skillSets(@RequestParam(required = false, defaultValue = "") String skillSetQuery, Model model) {
        final List<Skill> skills = skillSetsService.getSkillsForSkillSets(skillSetQuery);
        model.addAttribute(SKILLS, skills);
        model.addAttribute(SKILL_SET_QUERY, buildJsonOfSkillSetQuery(skillSetQuery));
        return SKILL_SETS;
    }

    // return search terms so token input can show them again (via its prePopulate mechanism)
    private List<Map<String, String>> buildJsonOfSkillSetQuery(String skillSetQuery) {
        if (skillSetQuery.isEmpty())
            return Collections.emptyList();

        return Arrays.stream(skillSetQuery.split(","))
                .map(this::toMap)
                .toList();
    }

    private Map<String, String> toMap(String s) {
        return Map.of("id", s, "name", s);
    }
}
