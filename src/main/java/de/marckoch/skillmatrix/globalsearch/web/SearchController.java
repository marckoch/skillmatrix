package de.marckoch.skillmatrix.globalsearch.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.DEVELOPERS;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILLS;
import static de.marckoch.skillmatrix.skills.web.ViewNames.EMPTY_SEARCH;
import static de.marckoch.skillmatrix.skills.web.ViewNames.SEARCH_RESULT;

/**
 * Controller for the global search box at the top right.
 * We search developers first. Then we try searching skills.
 */
@Controller
@AllArgsConstructor
class SearchController {

    private final DeveloperRepository devRepo;

    private final SkillRepository skillRepo;

    private final SearchResultHighlighter searchResultHighlighter;

    @GetMapping("/globalsearch")
    public String processFindForm(@RequestParam String query, Model model) {

        // keep it simple: first try developers, then skills
        // we could (should?) refactor this into one query to the database,
        // but for now this is good enough

        boolean somethingFound = false;

        Collection<Developer> developers = devRepo.findByQuery(query.toUpperCase());
        if (!developers.isEmpty()) {
            searchResultHighlighter.highlightDeveloperSearchMatches(developers, query);
            somethingFound = true;
        }
        model.addAttribute(DEVELOPERS, developers);

        Collection<Skill> skills = skillRepo.findByQuery(query.toUpperCase());
        if (!skills.isEmpty()) {
            searchResultHighlighter.highlightSkillSearchMatches(skills, query);
            somethingFound = true;
        }
        model.addAttribute(SKILLS, skills);

        if (somethingFound)
            return SEARCH_RESULT;
        else
            return EMPTY_SEARCH;
    }
}