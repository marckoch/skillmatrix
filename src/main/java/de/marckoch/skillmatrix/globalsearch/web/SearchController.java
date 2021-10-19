package de.marckoch.skillmatrix.globalsearch.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

import static de.marckoch.skillmatrix.skills.web.ViewNames.*;

/**
 * Controller for the global search box at the top right.
 * We search developers first. When we don't find anything we try searching skills.
 */
@Controller
class SearchController {

    private final DeveloperRepository devRepo;

    private final SkillRepository skillRepo;

    public SearchController(DeveloperRepository devRepo, SkillRepository skillRepo) {
        this.devRepo = devRepo;
        this.skillRepo = skillRepo;
    }

    @GetMapping("/globalsearch")
    public String processFindForm(@RequestParam String query, Model model) {

        // keep it simple: first try developers, then skills
        // we could (should?) refactor this into one query to the database,
        // but for now this is good enough

        Collection<Developer> developers = devRepo.findByQuery(query.toUpperCase());
        if (!developers.isEmpty()) {
            highlightDeveloperSearchMatches(developers, query);
            model.addAttribute("developers", developers);
            return DEVELOPER_LIST;
        }

        Collection<Skill> skills = skillRepo.findByQuery(query.toUpperCase());
        if (!skills.isEmpty()) {
            highlightSkillSearchMatches(skills, query);
            model.addAttribute("skills", skills);
            return SKILL_LIST;
        }

        return EMPTY_SEARCH;
    }

    private final String HIGHLIGHTED_RESULT = "<mark>$0</mark>";
    private void highlightDeveloperSearchMatches(Collection<Developer> developers, String query) {
        developers.forEach(d -> {
            if (d.getFirstName() != null) {
                d.setFirstName(d.getFirstName().replaceAll(caseIgnore(query), HIGHLIGHTED_RESULT));
            }
            if (d.getLastName() != null) {
                d.setLastName(d.getLastName().replaceAll(caseIgnore(query), HIGHLIGHTED_RESULT));
            }
        });
    }

    private void highlightSkillSearchMatches(Collection<Skill> skills, String query) {
        skills.forEach(s -> {
            if (s.getName() != null) {
                s.setName(s.getName().replaceAll(caseIgnore(query), HIGHLIGHTED_RESULT));
            }
            if (s.getAlias() != null)
                s.setAlias(s.getAlias().replaceAll(caseIgnore(query), HIGHLIGHTED_RESULT));
        });
    }

    private String caseIgnore(String query) {
        return "(?i)" + query;
    }
}
