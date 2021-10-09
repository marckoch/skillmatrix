package de.marckoch.skillmatrix.skills;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
class SkillController {

    private final SkillRepository skillRepository;

    private final DeveloperRepository developerRepository;

    @GetMapping("/skills")
    public String showAll() {
        return "redirect:skills/page/0?sort-field=name&sort-dir=asc";
    }

    @GetMapping("/skills/page/{pagenumber}")
    public String showAll(@PathVariable int pagenumber,
                          @RequestParam(name = "sort-field") final String sortField,
                          @RequestParam(name = "sort-dir") final String sortDir,
                          Model model) {
        Sort sort = build(sortDir, sortField);
        Pageable p = PageRequest.of(pagenumber, 10, sort);
        Page<Skill> resultPage = skillRepository.findAll(p);

        model.addAttribute("skills", resultPage);

        // paging
        model.addAttribute("currentPage", pagenumber);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());

        // sorting
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverse(sortDir));

        return "skills/skillList";
    }

    private Sort build(String sortDir, String sortField) {
        return "desc".equalsIgnoreCase(sortDir) ?
                Sort.by(sortField).descending() :
                Sort.by(sortField).ascending();
    }

    private String reverse(String sortDir) {
        return sortDir.equalsIgnoreCase("asc") ? "desc" : "asc";
    }

    @GetMapping("/skills/{skillId}")
    public ModelAndView showSkill(@PathVariable("skillId") int skillId) {
        ModelAndView mav = new ModelAndView("skills/skillDetails");
        Skill skill = skillRepository.findById(skillId).get();
        mav.addObject(skill);
        return mav;
    }

    @GetMapping("/skills/new")
    public String initCreationForm(Map<String, Object> model) {
        Skill skill = new Skill();
        model.put("skill", skill);
        return "/skills/createOrUpdateSkillForm";
    }

    @PostMapping("/skills/new")
    public String processCreationForm(@Valid Skill skill, BindingResult result) {
        if (result.hasErrors()) {
            return "/skills/createOrUpdateSkillForm";
        } else {
            skillRepository.save(skill);
            return "redirect:/skills/" + skill.getSkillId();
        }
    }

    @GetMapping("/skills/{skillId}/edit")
    public String initUpdateSkillForm(@PathVariable("skillId") int skillId, Model model) {
        Skill skill = skillRepository.findById(skillId).get();
        model.addAttribute(skill);
        return "/skills/createOrUpdateSkillForm";
    }

    @PostMapping("/skills/{skillId}/edit")
    public String processUpdateSkillForm(@Valid Skill skill, BindingResult result,
                                         @PathVariable("skillId") int skillId) {
        if (result.hasErrors()) {
            return "/skills/createOrUpdateSkillForm";
        } else {
            skill.setSkillId(skillId);
            skillRepository.save(skill);
            return "redirect:/skills/{skillId}";
        }
    }

    @GetMapping("/skills/matrix")
    public String skillMatrix(Model model) {
        List<Skill> allSkillsRanked = getAllSkillsRanked();

        List<Developer> allDevsRanked = getAllDevsRanked();

        List<Integer> idsOfAllDevelopers = allDevsRanked.stream().map(Developer::getDeveloperId).toList();

        allSkillsRanked.forEach(skill -> {
            List<Experience> experiences = skill.getExperiences();
            List<Integer> idsOfMissingDevs = findIdsOfMissingDevs(idsOfAllDevelopers, experiences);

            idsOfMissingDevs.forEach(devId -> {
                Experience e = createEmptyExperienceForDeveloper(devId);
                experiences.add(e);
            });

            // sort all experiences again by developer weight
            skill.getExperiences().sort(Comparator.comparing(o -> -o.getDeveloper().getWeight()));
        });

        model.addAttribute("skills", allSkillsRanked);
        return "/skills/matrix";
    }

    private List<Integer> findIdsOfMissingDevs(List<Integer> idsOfAllDevelopers, List<Experience> experiences) {
        final List<Integer> idsOfDevelopersWithThisSkill = experiences.stream()
                .map(exp -> exp.getDeveloper().getDeveloperId())
                .toList();
        return idsOfAllDevelopers.stream()
                .filter(id -> !idsOfDevelopersWithThisSkill.contains(id))
                .toList();
    }

    private Experience createEmptyExperienceForDeveloper(Integer id) {
        final Experience e = new Experience();
        e.setDeveloper(developerRepository.findById(id).get());
        e.setRating(0);
        e.setYears(0);
        return e;
    }

    private List<Developer> getAllDevsRanked() {
        Comparator<Developer> devWeightComp = Comparator.comparing(HasExperiences::getWeight);
        Comparator<Developer> devNameComp = Comparator.comparing(Developer::getLastName);

        return developerRepository.findAll()
                .stream()
                .sorted(devWeightComp.reversed().thenComparing(devNameComp))
                .collect(Collectors.toList());
    }

    private List<Skill> getAllSkillsRanked() {
        Comparator<Skill> skillWeightComp = Comparator.comparing(HasExperiences::getWeight);
        Comparator<Skill> skillNameComp = Comparator.comparing(Skill::getName);

        return skillRepository.findAll()
                .stream()
                .sorted(skillWeightComp.reversed().thenComparing(skillNameComp))
                .collect(Collectors.toList());
    }
}
