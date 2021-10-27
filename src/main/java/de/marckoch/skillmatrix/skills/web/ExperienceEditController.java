package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.ExperienceRepository;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.web.dto.ExperienceDTO;
import de.marckoch.skillmatrix.skills.web.dto.ExperienceMapper;
import de.marckoch.skillmatrix.skills.web.dto.SelectItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static de.marckoch.skillmatrix.skills.web.DeveloperEditController.REDIRECT_DEVELOPERS;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.DEVELOPER;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.EXPERIENCE_DTO;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILL_SELECT_ITEMS;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.WAS_VALIDATED;
import static de.marckoch.skillmatrix.skills.web.ViewNames.EXPERIENCE_EDIT_VIEW;

@Controller
@AllArgsConstructor
class ExperienceEditController {

    private final ExperienceRepository experienceRepository;

    private final DeveloperRepository developerRepository;

    private final ExperienceMapper experienceMapper;

    @PostMapping("/experience/{developerId}/new")
    public String processAddSkillToDeveloperForm(@PathVariable("developerId") int developerId,
                                                 @Valid ExperienceDTO experienceDTO, BindingResult result,
                                                 final RedirectAttributes redirectAttributes, Map<String, Object> model) {
        Developer dev = developerRepository.findById(developerId).orElseThrow();
        model.put(DEVELOPER, dev);

        if (result.hasErrors()) {
            // save (erroneous model) for redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.experienceDTO", result);
            redirectAttributes.addFlashAttribute(EXPERIENCE_DTO, experienceDTO);
        } else {
            Experience newExp = experienceMapper.createNewEntityFromDTO(experienceDTO);
            newExp.setDeveloper(dev);

            experienceRepository.save(newExp);
        }

        return "redirect:/developers/" + dev.getDeveloperId();
    }

    @GetMapping("/experience/edit/{experienceId}")
    public String initExperienceEditView(@PathVariable("experienceId") int experienceId, Model model) {
        Experience exp = experienceRepository.findById(experienceId).orElseThrow();
        ExperienceDTO dto = experienceMapper.buildExperienceDTO(exp);
        model.addAttribute(EXPERIENCE_DTO, dto);
        model.addAttribute(DEVELOPER, exp.getDeveloper());

        addSingleSelectItemToModel(exp, model);
        return EXPERIENCE_EDIT_VIEW;
    }

    @PostMapping("/experience/edit/{experienceId}")
    public String processExperienceEditView(@PathVariable("experienceId") int experienceId, Model model,
                                            @Valid ExperienceDTO experienceDTO, BindingResult result) {
        model.addAttribute(WAS_VALIDATED, true);
        if (result.hasErrors()) {
            Experience exp = experienceRepository.findById(experienceId).orElseThrow();
            model.addAttribute(DEVELOPER, exp.getDeveloper());

            addSingleSelectItemToModel(exp, model);

            return EXPERIENCE_EDIT_VIEW;
        } else {
            Experience existingExpEntity = experienceRepository.findById(experienceId).orElseThrow();
            existingExpEntity.setYears(experienceDTO.getYears());
            existingExpEntity.setRating(experienceDTO.getRating());

            Experience updatedExpEntity = experienceRepository.save(existingExpEntity);
            return REDIRECT_DEVELOPERS + updatedExpEntity.getDeveloper().getDeveloperId();
        }
    }

    @ModelAttribute("ratings")
    public List<SelectItem> getRatings() {
        return IntStream.range(1, 6).boxed()
                .map(i -> new SelectItem(i, i + (i == 1 ? " star" : " stars")))
                .toList();
    }

    @GetMapping("/experience/delete/{experienceId}")
    public String delete(@PathVariable("experienceId") int experienceId) {
        Developer dev = experienceRepository.findById(experienceId).orElseThrow().getDeveloper();
        dev.getExperiences().removeIf(e -> e.getExperienceId().equals(experienceId));
        developerRepository.save(dev);

        return "redirect:/developers/" + dev.getDeveloperId();
    }

    private void addSingleSelectItemToModel(Experience exp, Model model) {
        Skill skill = exp.getSkill();
        SelectItem selectItem = new SelectItem(skill.getSkillId(), skill.getNameAndVersion());
        model.addAttribute(SKILL_SELECT_ITEMS, List.of(selectItem));
    }
}
