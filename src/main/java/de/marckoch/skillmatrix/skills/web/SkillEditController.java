package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
@AllArgsConstructor
class SkillEditController {

    private static final String CREATE_OR_UPDATE_SKILL_VIEW = "/skills/createOrUpdateSkillForm";

    private final SkillRepository skillRepository;

    @GetMapping("/skills/new")
    public String initCreationForm(Map<String, Object> model) {
        Skill skill = new Skill();
        model.put("skill", skill);
        return CREATE_OR_UPDATE_SKILL_VIEW;
    }

    @PostMapping("/skills/new")
    public String processCreationForm(@Valid Skill skill, BindingResult result) {
        if (result.hasErrors()) {
            return CREATE_OR_UPDATE_SKILL_VIEW;
        } else {
            Skill savedSkill = skillRepository.save(skill);
            return "redirect:/skills/" + savedSkill.getSkillId();
        }
    }

    @GetMapping("/skills/{skillId}/edit")
    public String initUpdateSkillForm(@PathVariable("skillId") int skillId, Model model) {
        Skill skill = skillRepository.findById(skillId).orElseThrow();
        model.addAttribute(skill);
        return CREATE_OR_UPDATE_SKILL_VIEW;
    }

    @PostMapping("/skills/{skillId}/edit")
    public String processUpdateSkillForm(@Valid Skill skill, BindingResult result,
                                         @PathVariable("skillId") int skillId) {
        if (result.hasErrors()) {
            return CREATE_OR_UPDATE_SKILL_VIEW;
        } else {
            skill.setSkillId(skillId);
            Skill savedSkill = skillRepository.save(skill);
            return "redirect:/skills/" + savedSkill.getSkillId();
        }
    }
}
