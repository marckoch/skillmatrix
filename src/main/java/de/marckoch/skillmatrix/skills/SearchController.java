package de.marckoch.skillmatrix.skills;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.Map;

@Controller
class SearchController {

    private final DeveloperRepository devRepo;

    private final SkillRepository skillRepo;

    public SearchController(DeveloperRepository devRepo, SkillRepository skillRepo) {
        this.devRepo = devRepo;
        this.skillRepo = skillRepo;
    }

    @GetMapping("/search/init")
    public String initFindForm(Map<String, Object> model) {
        model.put("searchInput", new SearchInput());
        return "search/search";
    }

    @GetMapping("/search/search")
    public String processFindForm(SearchInput searchInput, BindingResult result, Map<String, Object> model) {

        // keep it simple: first try developers, then skills
        // we could (should?) refactor this into one query to the database,
        // but for now this is good enough

        Collection<Developer> developers = devRepo.findByQuery(searchInput.getQuery().toUpperCase());
        if (!developers.isEmpty()) {
            model.put("developers", developers);
            return "developers/developerList";
        }

        Collection<Skill> skills = skillRepo.findByQuery(searchInput.getQuery().toUpperCase());
        if (!skills.isEmpty()) {
            model.put("skills", skills);
            return "skills/skillList";
        }

        result.rejectValue("query", "notFound", "nothing found");
        return "search/search";
    }
}
