package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.web.dto.DeveloperDTO;
import de.marckoch.skillmatrix.skills.web.dto.DeveloperMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.DEVELOPER;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.DEVELOPER_DTO;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.WAS_VALIDATED;
import static de.marckoch.skillmatrix.skills.web.ViewNames.CREATE_OR_UPDATE_DEVELOPER_VIEW;

@Controller
@AllArgsConstructor
class DeveloperEditController {

	public static final String REDIRECT_DEVELOPERS = "redirect:/developers/";

	private final DeveloperRepository developerRepository;

	private final DeveloperMapper developerMapper;

	@GetMapping("/developers/new")
	public String initCreationForm(Model model) {
		DeveloperDTO developerDTO = new DeveloperDTO();
		model.addAttribute(DEVELOPER_DTO, developerDTO);
		return CREATE_OR_UPDATE_DEVELOPER_VIEW;
	}

	@PostMapping("/developers/new")
	public String processCreationForm(@Valid DeveloperDTO developerDTO, BindingResult result, Model model) {
		model.addAttribute(WAS_VALIDATED, true);
		if (result.hasErrors()) {
			return CREATE_OR_UPDATE_DEVELOPER_VIEW;
		} else {
			Developer newDev = new Developer();
			developerMapper.updateEntityFromDTO(developerDTO, newDev);
			Developer savedDev = developerRepository.save(newDev);
			return REDIRECT_DEVELOPERS + savedDev.getDeveloperId();
		}
	}

	@GetMapping("/developers/{developerId}/edit")
	public String initUpdateDeveloperForm(@PathVariable("developerId") int developerId, Model model) {
		Developer developer = developerRepository.findById(developerId).orElseThrow();

		DeveloperDTO dto = developerMapper.buildDeveloperDTO(developer);
		model.addAttribute(DEVELOPER_DTO, dto);
		return CREATE_OR_UPDATE_DEVELOPER_VIEW;
	}

	@PostMapping("/developers/{developerId}/edit")
	public String processUpdateDeveloperForm(@Valid DeveloperDTO developerDTO, BindingResult result,
										 	 @PathVariable("developerId") int developerId, Model model) {
		model.addAttribute(WAS_VALIDATED, true);
		if (result.hasErrors()) {
			return CREATE_OR_UPDATE_DEVELOPER_VIEW;
		} else {
			Developer existingDev = developerRepository.findById(developerId).orElseThrow();

			developerMapper.updateEntityFromDTO(developerDTO, existingDev);

			Developer savedDev = developerRepository.save(existingDev);
			model.addAttribute(DEVELOPER, savedDev);
			return REDIRECT_DEVELOPERS + savedDev.getDeveloperId();
		}
	}
}
