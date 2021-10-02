package de.marckoch.skillmatrix.skills;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@Controller
@AllArgsConstructor
class SkillController {

	private final SkillRepository skillRepository;

	@GetMapping("/skills")
	public ModelAndView showAll() {
		ModelAndView mav = new ModelAndView("skills/skillList");
		Collection<Skill> skills = skillRepository.findAll();
		mav.getModel().put("skills", skills);
		return mav;
	}

	@GetMapping("/skills/{skillId}")
	public ModelAndView showSkill(@PathVariable("skillId") int skillId) {
		ModelAndView mav = new ModelAndView("skills/skillDetails");
		Skill skill = skillRepository.findById(skillId).get();
		mav.addObject(skill);
		return mav;
	}

	@GetMapping("/skills/new")
	public String initCreationForm(Map<String, Object> model) {
		Skill skill = new Skill();
		model.put("skill", skill);
		return "/skills/createOrUpdateSkillForm";
	}

	@PostMapping("/skills/new")
	public String processCreationForm(@Valid Skill skill, BindingResult result) {
		if (result.hasErrors()) {
			return "/skills/createOrUpdateSkillForm";
		} else {
			skillRepository.save(skill);
			return "redirect:/skills/" + skill.getSkillId();
		}
	}

	@GetMapping("/skills/{skillId}/edit")
	public String initUpdateSkillForm(@PathVariable("skillId") int skillId, Model model) {
		Skill skill = skillRepository.findById(skillId).get();
		model.addAttribute(skill);
		return "/skills/createOrUpdateSkillForm";
	}

	@PostMapping("/skills/{skillId}/edit")
	public String processUpdateSkillForm(@Valid Skill skill, BindingResult result,
										 @PathVariable("skillId") int skillId) {
		if (result.hasErrors()) {
			return "/skills/createOrUpdateSkillForm";
		} else {
			skill.setSkillId(skillId);
			skillRepository.save(skill);
			return "redirect:/skills/{skillId}";
		}
	}
}
