package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILL;
import static de.marckoch.skillmatrix.skills.web.ViewNames.SKILL_DETAILS;

@Controller
@AllArgsConstructor
class SkillDetailsController {

    private final SkillRepository skillRepository;

    @GetMapping("/skills/{skillId}")
    public String showSkill(@PathVariable("skillId") int skillId,
                            @RequestParam(name = "sort-field", required = false, defaultValue = "weight") final String sortField,
                            @RequestParam(name = "sort-dir", required = false, defaultValue = "desc") final String sortDir,
                            Model model) {
        Skill skill = skillRepository.findById(skillId).orElseThrow();

        sortExperiences(skill, sortField, sortDir);

        SortUtil.addSortAttributesToModel(model, sortField, sortDir);

        model.addAttribute(SKILL.modelAttributeName, skill);
        return SKILL_DETAILS;
    }

    private void sortExperiences(Skill skill, String sortField, String sortDir) {
        final Comparator<Experience> byWeight = Comparator.comparing(Experience::getWeight);
        final Comparator<Experience> byRating = Comparator.comparing(Experience::getRating);
        final Comparator<Experience> byDeveloperFullName = Comparator.comparing(o -> o.getDeveloper().getFullName());

        switch (sortField) {
            case "name":
                if ("asc".equalsIgnoreCase(sortDir))
                    skill.getExperiences().sort(byDeveloperFullName);
                else
                    skill.getExperiences().sort(byDeveloperFullName.reversed());
                break;
            case "rating":
                if ("asc".equalsIgnoreCase(sortDir))
                    skill.getExperiences().sort(byRating);
                else
                    skill.getExperiences().sort(byRating.reversed());
                break;
            case "weight":
                if ("asc".equalsIgnoreCase(sortDir))
                    skill.getExperiences().sort(byWeight);
                else
                    skill.getExperiences().sort(byWeight.reversed());
                break;
            default:
                skill.getExperiences().sort(byWeight.reversed());
        }
    }
}
