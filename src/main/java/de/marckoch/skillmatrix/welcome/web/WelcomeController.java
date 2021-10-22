package de.marckoch.skillmatrix.welcome.web;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SHOW_WARNING;

@Controller
@AllArgsConstructor
class WelcomeController {

	private final Environment environment;

	@GetMapping("/")
	public String welcome(Model model) {
		boolean noActiveProfile = Arrays.asList(environment.getActiveProfiles()).isEmpty();
		model.addAttribute(SHOW_WARNING, noActiveProfile);
		return "welcome";
	}
}
