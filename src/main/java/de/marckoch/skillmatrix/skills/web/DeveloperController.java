package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.service.SkillsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Controller
@AllArgsConstructor
class DeveloperController {

	private static final String CREATE_OR_UPDATE_DEVELOPER_VIEW = "/developers/createOrUpdateDeveloperForm";

	private final DeveloperRepository developerRepository;

	private final SkillsService skillsService;

	@GetMapping("/developers")
	public String showAll() {
		return "redirect:developers/page/0?sort-field=lastName&sort-dir=asc";
	}

	@GetMapping("/developers/page/{pagenumber}")
	public String showAll(@PathVariable int pagenumber,
						  @RequestParam(name = "sort-field") final String sortField,
						  @RequestParam(name = "sort-dir") final String sortDir,
						  Model model) {
		Sort sort = SortUtil.build(sortDir, sortField);
		Pageable p = PageRequest.of(pagenumber, 10, sort);
		Page<Developer> resultPage = developerRepository.findAll(p);

		model.addAttribute("developers", resultPage);

		SortUtil.addPagingAndSortAttributesToModel(model, resultPage, pagenumber, sortField, sortDir);

		return "developers/developerList";
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

		List<Skill> freeSkills = skillsService.getFreeSkills(dev);
		List<SelectItem> selectItems = freeSkills.stream()
				.map(this::skill2SelectItem)
				.sorted(Comparator.comparing(SelectItem::getValue))
				.toList();
		mav.getModel().put("skillSelectItems", selectItems);

		return mav;
	}

	private SelectItem skill2SelectItem(Skill skill) {
		return new SelectItem(skill.getSkillId(), skill.getNameAndVersion());
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
			Developer savedDev = developerRepository.save(developer);
			return "redirect:/developers/" + savedDev.getDeveloperId();
		}
	}

	@ModelAttribute("ratings")
	public List<SelectItem> getRatings() {
		return IntStream.range(1, 6).boxed()
				.map(i -> new SelectItem(i, i + " stars"))
				.toList();
	}
}
