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

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILL_DTO;

@Controller
@AllArgsConstructor
class SkillEditController {

    public static final String CREATE_OR_UPDATE_SKILL_VIEW = "/skills/createOrUpdateSkillForm";
    public static final String REDIRECT_SKILLS = "redirect:/skills/";

    private final SkillRepository skillRepository;

    @GetMapping("/skills/new")
    public String initCreationForm(Map<String, Object> model) {
        SkillDTO skill = new SkillDTO();
        model.put(SKILL_DTO.modelAttributeName(), skill);
        return CREATE_OR_UPDATE_SKILL_VIEW;
    }

    @PostMapping("/skills/new")
    public String processCreationForm(@Valid SkillDTO skillDTO, BindingResult result) {
        if (result.hasErrors()) {
            return CREATE_OR_UPDATE_SKILL_VIEW;
        } else {
            Skill newSkill = new Skill();
            updateEntityFromDTO(skillDTO, newSkill);
            Skill savedSkill = skillRepository.save(newSkill);
            return REDIRECT_SKILLS + savedSkill.getSkillId();
        }
    }

    @GetMapping("/skills/{skillId}/edit")
    public String initUpdateSkillForm(@PathVariable("skillId") int skillId, Model model) {
        Skill skill = skillRepository.findById(skillId).orElseThrow();

        SkillDTO dto = buildSkillDTO(skill);
        model.addAttribute(SKILL_DTO.modelAttributeName(), dto);
        return CREATE_OR_UPDATE_SKILL_VIEW;
    }

    @PostMapping("/skills/{skillId}/edit")
    public String processUpdateSkillForm(@Valid SkillDTO skillDTO, BindingResult result,
                                         @PathVariable("skillId") int skillId, Model model) {
        if (result.hasErrors()) {
            return CREATE_OR_UPDATE_SKILL_VIEW;
        } else {
            Skill existingSkill = skillRepository.findById(skillId).orElseThrow();

            updateEntityFromDTO(skillDTO, existingSkill);

            Skill savedSkill = skillRepository.save(existingSkill);
            model.addAttribute(SKILL_DTO.modelAttributeName(), savedSkill);
            return REDIRECT_SKILLS + savedSkill.getSkillId();
        }
    }

    private SkillDTO buildSkillDTO(Skill skill) {
        return SkillDTO.builder()
                .skillId(skill.getSkillId())
                .name(skill.getName())
                .version(skill.getVersion())
                .alias(skill.getAlias())
                .build();
    }

    private void updateEntityFromDTO(SkillDTO dto, Skill entity) {
        entity.setName(dto.getName());
        entity.setVersion(dto.getVersion());
        entity.setAlias(dto.getAlias());
    }
}
