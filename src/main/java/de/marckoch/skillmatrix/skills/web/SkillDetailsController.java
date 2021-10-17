package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILL;

@Controller
@AllArgsConstructor
class SkillDetailsController {

    private final SkillRepository skillRepository;

    @GetMapping("/skills/{skillId}")
    public ModelAndView showSkill(@PathVariable("skillId") int skillId) {
        ModelAndView mav = new ModelAndView("skills/skillDetails");
        Skill skill = skillRepository.findById(skillId).orElseThrow();
        mav.addObject(SKILL.modelAttributeName, skill);
        return mav;
    }
}
