package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILL_DTO;
import static de.marckoch.skillmatrix.skills.web.ViewNames.SKILL_DETAILS;

@Controller
@AllArgsConstructor
class SkillDetailsController {

    private final SkillRepository skillRepository;

    private final SkillMapper skillMapper;

    private final ExperienceMapper experienceMapper;

    @GetMapping("/skills/{skillId}")
    public ModelAndView showSkill(@PathVariable("skillId") int skillId) {
        ModelAndView mav = new ModelAndView(SKILL_DETAILS);

        Skill skill = skillRepository.findById(skillId).orElseThrow();
        SkillDTO skillDTO = skillMapper.buildSkillDTO(skill);
        mav.addObject(SKILL_DTO.modelAttributeName, skillDTO);

        List<ExperienceDTO> experienceDTOList = skill.getExperiences()
                .stream()
                .map(experienceMapper::buildDTO)
                .sorted(Comparator.comparing(ExperienceDTO::getWeight).reversed())
                .toList();
        mav.addObject("experienceDTOs", experienceDTOList);

        return mav;
    }
}
