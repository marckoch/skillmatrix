package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
class DeveloperEditController {

	private static final String CREATE_OR_UPDATE_DEVELOPER_VIEW = "/developers/createOrUpdateDeveloperForm";

	private final DeveloperRepository developerRepository;

	@GetMapping("/developers/new")
	public String initCreationForm(Model model) {
		DeveloperDTO developer = new DeveloperDTO();
		model.addAttribute("developerDTO", developer);
		return CREATE_OR_UPDATE_DEVELOPER_VIEW;
	}

	@PostMapping("/developers/new")
	public String processCreationForm(@Valid DeveloperDTO developerDTO, BindingResult result) {
		if (result.hasErrors()) {
			return CREATE_OR_UPDATE_DEVELOPER_VIEW;
		} else {
			Developer newDev = new Developer();
			updateEntityFromDTO(developerDTO, newDev);
			Developer savedDev = developerRepository.save(newDev);
			return "redirect:/developers/" + savedDev.getDeveloperId();
		}
	}

	@GetMapping("/developers/{developerId}/edit")
	public String initUpdateDeveloperForm(@PathVariable("developerId") int developerId, Model model) {
		Developer developer = developerRepository.findById(developerId).orElseThrow();

		DeveloperDTO dto = buildDeveloperDTO(developer);
		model.addAttribute("developerDTO", dto);
		return CREATE_OR_UPDATE_DEVELOPER_VIEW;
	}

	@PostMapping("/developers/{developerId}/edit")
	public String processUpdateDeveloperForm(@Valid DeveloperDTO developerDTO, BindingResult result,
										 	 @PathVariable("developerId") int developerId, Model model) {
		if (result.hasErrors()) {
			return CREATE_OR_UPDATE_DEVELOPER_VIEW;
		} else {
			Developer existingDev = developerRepository.findById(developerId).orElseThrow();

			updateEntityFromDTO(developerDTO, existingDev);

			Developer savedDev = developerRepository.save(existingDev);
			model.addAttribute("developer", savedDev);
			return "redirect:/developers/" + savedDev.getDeveloperId();
		}
	}

	private DeveloperDTO buildDeveloperDTO(Developer developer) {
		return DeveloperDTO.builder()
				.developerId(developer.getDeveloperId())
				.firstName(developer.getFirstName())
				.lastName(developer.getLastName())
				.title(developer.getTitle())
				.build();
	}

	private void updateEntityFromDTO(DeveloperDTO dto, Developer entity) {
		entity.setLastName(dto.getLastName());
		entity.setFirstName(dto.getFirstName());
		entity.setTitle(dto.getTitle());
	}
}
