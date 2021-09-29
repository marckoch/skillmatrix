package de.marckoch.skillmatrix.skills;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
class DeveloperController {

	private final DeveloperRepository developers;

	public DeveloperController(DeveloperRepository developers) {
		this.developers = developers;
	}

	@GetMapping("/developers")
	public ModelAndView showAll() {
		ModelAndView mav = new ModelAndView("developers/developerList");
		Collection<Developer> devs = this.developers.findAll();
		System.out.println("===" + devs);
		mav.getModel().put("developers", devs);
		return mav;
	}

	@GetMapping("/developers/{developerId}")
	public ModelAndView showDeveloper(@PathVariable("developerId") int developerId) {
		ModelAndView mav = new ModelAndView("developers/developerDetails");
		Developer dev = this.developers.findById(developerId);
		mav.addObject(dev);
		return mav;
	}
}
