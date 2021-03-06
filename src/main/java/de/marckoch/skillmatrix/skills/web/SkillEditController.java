package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import de.marckoch.skillmatrix.skills.web.dto.SkillDTO;
import de.marckoch.skillmatrix.skills.web.dto.SkillMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILL_DTO;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.WAS_VALIDATED;
import static de.marckoch.skillmatrix.skills.web.ViewNames.CREATE_OR_UPDATE_SKILL_VIEW;
import static de.marckoch.skillmatrix.skills.web.ViewNames.REDIRECT_SKILLS;

@Controller
@AllArgsConstructor
class SkillEditController {

    private final SkillRepository skillRepository;

    private final SkillMapper skillMapper;

    @GetMapping("/skills/new")
    public String initCreationForm(Map<String, Object> model) {
        SkillDTO skill = new SkillDTO();
        model.put(SKILL_DTO, skill);
        return CREATE_OR_UPDATE_SKILL_VIEW;
    }

    @PostMapping("/skills/new")
    public String processCreationForm(@Valid SkillDTO skillDTO, BindingResult result, Model model) {
        model.addAttribute(WAS_VALIDATED, true);
        if (result.hasErrors()) {
            return CREATE_OR_UPDATE_SKILL_VIEW;
        } else {
            Skill newSkill = new Skill();
            skillMapper.updateEntityFromDTO(skillDTO, newSkill);
            Skill savedSkill = skillRepository.save(newSkill);
            return REDIRECT_SKILLS + '/' + savedSkill.getSkillId();
        }
    }

    @PostMapping(value = "/skills/new", params = "cancel")
    public String processCreationFormCancel() {
        return REDIRECT_SKILLS;
    }

    @GetMapping("/skills/{skillId}/edit")
    public String initUpdateSkillForm(@PathVariable("skillId") int skillId, Model model) {
        Skill skill = skillRepository.findById(skillId).orElseThrow();

        SkillDTO dto = skillMapper.buildSkillDTO(skill);
        model.addAttribute(SKILL_DTO, dto);
        return CREATE_OR_UPDATE_SKILL_VIEW;
    }

    @PostMapping("/skills/{skillId}/edit")
    public String processUpdateSkillForm(@Valid SkillDTO skillDTO, BindingResult result,
                                         @PathVariable("skillId") int skillId, Model model) {
        model.addAttribute(WAS_VALIDATED, true);
        if (result.hasErrors()) {
            return CREATE_OR_UPDATE_SKILL_VIEW;
        } else {
            Skill existingSkill = skillRepository.findById(skillId).orElseThrow();

            skillMapper.updateEntityFromDTO(skillDTO, existingSkill);

            Skill savedSkill = skillRepository.save(existingSkill);
            model.addAttribute(SKILL_DTO, savedSkill);
            return REDIRECT_SKILLS + '/' + savedSkill.getSkillId();
        }
    }

    @PostMapping(value = "/skills/{skillId}/edit", params = "cancel")
    public String processUpdateSkillFormCancel(@PathVariable("skillId") int skillId) {
        return REDIRECT_SKILLS + '/' +  skillId;
    }
}
