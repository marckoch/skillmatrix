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
class DeveloperController {

	private final DeveloperRepository developerRepository;

	@GetMapping("/developers")
	public ModelAndView showAll() {
		ModelAndView mav = new ModelAndView("developers/developerList");
		Collection<Developer> devs = developerRepository.findAll();
		mav.getModel().put("developers", devs);
		return mav;
	}

	@GetMapping("/developers/{developerId}")
	public ModelAndView showDeveloper(@PathVariable("developerId") int developerId) {
		ModelAndView mav = new ModelAndView("developers/developerDetails");
		Developer dev = developerRepository.findById(developerId).get();
		mav.addObject(dev);
		return mav;
	}

	@GetMapping("/developers/new")
	public String initCreationForm(Map<String, Object> model) {
		Developer developer = new Developer();
		model.put("developer", developer);
		return "/developers/createOrUpdateDeveloperForm";
	}

	@PostMapping("/developers/new")
	public String processCreationForm(@Valid Developer developer, BindingResult result) {
		if (result.hasErrors()) {
			return "/developers/createOrUpdateDeveloperForm";
		} else {
			developerRepository.save(developer);
			return "redirect:/developers/" + developer.getDeveloperId();
		}
	}

	@GetMapping("/developers/{developerId}/edit")
	public String initUpdateDeveloperForm(@PathVariable("developerId") int developerId, Model model) {
		Developer developer = developerRepository.findById(developerId).get();
		model.addAttribute(developer);
		return "/developers/createOrUpdateDeveloperForm";
	}

	@PostMapping("/developers/{developerId}/edit")
	public String processUpdateDeveloperForm(@Valid Developer developer, BindingResult result,
										 	 @PathVariable("developerId") int developerId) {
		if (result.hasErrors()) {
			return "/developers/createOrUpdateDeveloperForm";
		} else {
			developer.setDeveloperId(developerId);
			developerRepository.save(developer);
			return "redirect:/developers/{developerId}";
		}
	}
}
