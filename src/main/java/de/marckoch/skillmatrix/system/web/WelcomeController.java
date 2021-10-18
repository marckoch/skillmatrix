package de.marckoch.skillmatrix.system.web;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
@AllArgsConstructor
class WelcomeController {

	private final Environment environment;

	@GetMapping("/")
	public String welcome(Model model) {
		boolean noActiveProfile = Arrays.asList(environment.getActiveProfiles()).isEmpty();
		model.addAttribute("showWarning", noActiveProfile);
		return "welcome";
	}

}
