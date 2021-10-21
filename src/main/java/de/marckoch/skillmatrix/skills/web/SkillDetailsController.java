package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILL;
import static de.marckoch.skillmatrix.skills.web.ViewNames.SKILL_DETAILS;

@Controller
@AllArgsConstructor
class SkillDetailsController {

    private final SkillRepository skillRepository;

    @GetMapping("/skills/{skillId}")
    public ModelAndView showSkill(@PathVariable("skillId") int skillId) {
        ModelAndView mav = new ModelAndView(SKILL_DETAILS);

        Skill skill = skillRepository.findById(skillId).orElseThrow();
        skill.getExperiences().sort(Comparator.comparing(Experience::getWeight).reversed());
        mav.addObject(SKILL.modelAttributeName, skill);
        return mav;
    }
}
