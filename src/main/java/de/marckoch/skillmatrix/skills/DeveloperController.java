package de.marckoch.skillmatrix.skills;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@AllArgsConstructor
class DeveloperController {

	private static final String CREATE_OR_UPDATE_DEVELOPER_VIEW = "/developers/createOrUpdateDeveloperForm";

	private final DeveloperRepository developerRepository;

	private final SkillRepository skillRepository;

	@GetMapping("/developers")
	public ModelAndView showAll() {
		ModelAndView mav = new ModelAndView("developers/developerList");
		Collection<Developer> devs = developerRepository.findAll();
		mav.getModel().put("developers", devs);
		return mav;
	}

	@GetMapping("/developers/{developerId}")
	public ModelAndView showDeveloper(@PathVariable("developerId") int developerId, Model model) {
		ModelAndView mav = new ModelAndView("developers/developerDetails");
		Developer dev = developerRepository.findById(developerId).orElseThrow();
		mav.addObject(dev);

		final String EXPERIENCE = "experience";

		// check if we already have a (erroneous) experience in model,
		// this was checked and put there by redirect from ExperienceController
		if (!model.containsAttribute(EXPERIENCE)) {
			Experience experience = new Experience();
			experience.setDeveloper(dev);
			mav.getModel().put(EXPERIENCE, experience);
		} else {
			mav.getModel().put(EXPERIENCE, model.getAttribute(EXPERIENCE));
		}

		mav.getModel().put("skillSelectItems", getFreeSkills(dev));

		return mav;
	}

	@GetMapping("/developers/new")
	public String initCreationForm(Map<String, Object> model) {
		Developer developer = new Developer();
		model.put("developer", developer);
		return CREATE_OR_UPDATE_DEVELOPER_VIEW;
	}

	@PostMapping("/developers/new")
	public String processCreationForm(@Valid Developer developer, BindingResult result) {
		if (result.hasErrors()) {
			return CREATE_OR_UPDATE_DEVELOPER_VIEW;
		} else {
			Developer savedDev = developerRepository.save(developer);
			return "redirect:/developers/" + savedDev.getDeveloperId();
		}
	}

	@GetMapping("/developers/{developerId}/edit")
	public String initUpdateDeveloperForm(@PathVariable("developerId") int developerId, Model model) {
		Developer developer = developerRepository.findById(developerId).orElseThrow();
		model.addAttribute(developer);
		return CREATE_OR_UPDATE_DEVELOPER_VIEW;
	}

	@PostMapping("/developers/{developerId}/edit")
	public String processUpdateDeveloperForm(@Valid Developer developer, BindingResult result,
										 	 @PathVariable("developerId") int developerId) {
		if (result.hasErrors()) {
			return CREATE_OR_UPDATE_DEVELOPER_VIEW;
		} else {
			developer.setDeveloperId(developerId);
			developerRepository.save(developer);
			return "redirect:/developers/{developerId}";
		}
	}

	// find all Skills that this developer does NOT have
	public List<SelectItem> getFreeSkills(Developer dev) {
		var skillsOfDeveloper = dev.getExperiences()
				.stream()
				.map(exp -> exp.getSkill().getSkillId())
				.toList();

		return skillRepository.findAll().stream()
				.filter(skill -> !skillsOfDeveloper.contains(skill.getSkillId()))
				.map(this::skill2SelectItem)
				.sorted(Comparator.comparing(SelectItem::getValue))
				.collect(Collectors.toList());
	}

	@ModelAttribute("ratings")
	public List<SelectItem> getRatings() {
		return IntStream.range(1, 6).boxed()
				.map(i -> new SelectItem(i, i + " stars"))
				.toList();
	}

	private SelectItem skill2SelectItem(Skill skill) {
		return new SelectItem(skill.getSkillId(), skill.getNameAndVersion());
	}

	@Getter
	@AllArgsConstructor
	static class SelectItem {
		Integer key;
		String value;
	}
}
