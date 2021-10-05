package de.marckoch.skillmatrix.skills;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@Controller
class SearchController {

    private final DeveloperRepository devRepo;

    private final SkillRepository skillRepo;

    public SearchController(DeveloperRepository devRepo, SkillRepository skillRepo) {
        this.devRepo = devRepo;
        this.skillRepo = skillRepo;
    }

    @GetMapping("/search")
    public String processFindForm(@RequestParam String query, Model model) {

        // keep it simple: first try developers, then skills
        // we could (should?) refactor this into one query to the database,
        // but for now this is good enough

        Collection<Developer> developers = devRepo.findByQuery(query.toUpperCase());
        if (!developers.isEmpty()) {
            model.addAttribute("developers", developers);
            return "developers/developerList";
        }

        Collection<Skill> skills = skillRepo.findByQuery(query.toUpperCase());
        if (!skills.isEmpty()) {
            model.addAttribute("skills", skills);
            return "skills/skillList";
        }

        return "search/emptySearch";
    }
}
