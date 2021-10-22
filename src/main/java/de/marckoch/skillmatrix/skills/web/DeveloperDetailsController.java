package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.service.SkillsService;
import de.marckoch.skillmatrix.skills.web.dto.ExperienceDTO;
import de.marckoch.skillmatrix.skills.web.dto.SelectItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.EXPERIENCE_DTO;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILL_SELECT_ITEMS;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.WAS_VALIDATED;
import static de.marckoch.skillmatrix.skills.web.ViewNames.DEVELOPER_DETAILS;

@Controller
@AllArgsConstructor
class DeveloperDetailsController {

    private final DeveloperRepository developerRepository;

    private final SkillsService skillsService;

    @GetMapping("/developers/{developerId}")
    public String showDeveloper(@PathVariable("developerId") int developerId,
                                @RequestParam(name = RequestParams.SORT_FIELD, required = false, defaultValue = "weight") final String sortField,
                                @RequestParam(name = RequestParams.SORT_DIR, required = false, defaultValue = "desc") final String sortDir,
                                Model model) {
        Developer dev = developerRepository.findById(developerId).orElseThrow();

        ExperienceSorter.sortExperiences(dev.getExperiences(), sortField, sortDir);

        SortUtil.addSortAttributesToModel(model, sortField, sortDir);

        model.addAttribute(dev);

        // check if we already have a (erroneous) experience in model,
        // this was checked and put there by redirect from ExperienceController
        if (model.containsAttribute(EXPERIENCE_DTO)) {
            model.addAttribute(WAS_VALIDATED, true);
            model.addAttribute(EXPERIENCE_DTO, model.getAttribute(EXPERIENCE_DTO));
        } else {
            ExperienceDTO experienceDTO = ExperienceDTO.builder()
                    .developer(dev)
                    .build();
            model.addAttribute(EXPERIENCE_DTO, experienceDTO);
        }

        List<Skill> freeSkills = skillsService.getFreeSkills(dev);
        List<SelectItem> selectItems = freeSkills.stream()
                .map(this::skill2SelectItem)
                .sorted(Comparator.comparing(SelectItem::getValue))
                .toList();
        model.addAttribute(SKILL_SELECT_ITEMS, selectItems);

        return DEVELOPER_DETAILS;
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
