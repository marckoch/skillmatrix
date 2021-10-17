package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.service.SkillsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.EXPERIENCE_DTO;

@Controller
@AllArgsConstructor
class DeveloperDetailsController {

    private final DeveloperRepository developerRepository;

    private final SkillsService skillsService;

    @GetMapping("/developers/{developerId}")
    public ModelAndView showDeveloper(@PathVariable("developerId") int developerId, Model model) {
        ModelAndView mav = new ModelAndView("developers/developerDetails");
        Developer dev = developerRepository.findById(developerId).orElseThrow();
        mav.addObject(dev);

        // check if we already have a (erroneous) experience in model,
        // this was checked and put there by redirect from ExperienceController
        if (!model.containsAttribute(EXPERIENCE_DTO.modelAttributeName)) {
            ExperienceDTO experienceDTO = new ExperienceDTO();
            experienceDTO.setDeveloper(dev);
            mav.getModel().put(EXPERIENCE_DTO.modelAttributeName, experienceDTO);
        } else {
            mav.getModel().put(EXPERIENCE_DTO.modelAttributeName, model.getAttribute(EXPERIENCE_DTO.modelAttributeName));
        }

        List<Skill> freeSkills = skillsService.getFreeSkills(dev);
        List<SelectItem> selectItems = freeSkills.stream()
                .map(this::skill2SelectItem)
                .sorted(Comparator.comparing(SelectItem::getValue))
                .toList();
        mav.getModel().put("skillSelectItems", selectItems);

        return mav;
    }

    @ModelAttribute("ratings")
    public List<SelectItem> getRatings() {
        return IntStream.range(1, 6).boxed()
                .map(i -> new SelectItem(i, i + (i == 1 ? " star" : " stars")))
                .toList();
    }

    private SelectItem skill2SelectItem(Skill skill) {
        return new SelectItem(skill.getSkillId(), skill.getNameAndVersion());
    }
}
