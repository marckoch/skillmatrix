package de.marckoch.skillmatrix.skills;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
class SkillController {

	private final SkillRepository skillRepository;

	public SkillController(SkillRepository skillRepository) {
		this.skillRepository = skillRepository;
	}

	@GetMapping("/skills")
	public ModelAndView showAll() {
		ModelAndView mav = new ModelAndView("skills/skillList");
		Collection<Skill> skills = skillRepository.findAll();
		System.out.println("===" + skills);
		mav.getModel().put("skills", skills);
		return mav;
	}

	@GetMapping("/skills/{skillId}")
	public ModelAndView showDeveloper(@PathVariable("skillId") int skillId) {
		ModelAndView mav = new ModelAndView("skills/skillDetails");
		Skill skill = skillRepository.findById(skillId);
		mav.addObject(skill);
		return mav;
	}
}
