package de.marckoch.skillmatrix.globalsearch.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import de.marckoch.skillmatrix.skills.web.ViewNames;
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
            model.addAttribute("developers", developers);
            return DEVELOPER_LIST;
        }

        Collection<Skill> skills = skillRepo.findByQuery(query.toUpperCase());
        if (!skills.isEmpty()) {
            model.addAttribute("skills", skills);
            return SKILL_LIST;
        }

        return EMPTY_SEARCH;
    }
}
