package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILL;
import static de.marckoch.skillmatrix.skills.web.RequestParams.SORT_DIR;
import static de.marckoch.skillmatrix.skills.web.RequestParams.SORT_FIELD;
import static de.marckoch.skillmatrix.skills.web.SortDirection.DESC;
import static de.marckoch.skillmatrix.skills.web.SortField.WEIGHT;
import static de.marckoch.skillmatrix.skills.web.ViewNames.SKILL_DETAILS;

@Controller
@AllArgsConstructor
class SkillDetailsController {

    private final SkillRepository skillRepository;

    @GetMapping("/skills/{skillId}")
    public String showSkill(@PathVariable("skillId") int skillId,
                            @RequestParam(name = SORT_FIELD, required = false, defaultValue = WEIGHT) final String sortField,
                            @RequestParam(name = SORT_DIR, required = false, defaultValue = DESC) final String sortDir,
                            Model model) {
        Skill skill = skillRepository.findById(skillId).orElseThrow();

        ExperienceSorter.sortExperiences(skill.getExperiences(), sortField, sortDir);

        SortUtil.addSortAttributesToModel(model, sortField, sortDir);

        model.addAttribute(SKILL, skill);
        return SKILL_DETAILS;
    }
}
