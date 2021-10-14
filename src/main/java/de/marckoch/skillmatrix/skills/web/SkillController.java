package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
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
import java.util.Map;

@Controller
@AllArgsConstructor
class SkillController {

    private static final String CREATE_OR_UPDATE_SKILL_VIEW = "/skills/createOrUpdateSkillForm";

    private final SkillRepository skillRepository;

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
        Skill skill = skillRepository.findById(skillId).orElseThrow();
        mav.addObject(skill);
        return mav;
    }

    @GetMapping("/skills/new")
    public String initCreationForm(Map<String, Object> model) {
        Skill skill = new Skill();
        model.put("skill", skill);
        return CREATE_OR_UPDATE_SKILL_VIEW;
    }

    @PostMapping("/skills/new")
    public String processCreationForm(@Valid Skill skill, BindingResult result) {
        if (result.hasErrors()) {
            return CREATE_OR_UPDATE_SKILL_VIEW;
        } else {
            Skill savedSkill = skillRepository.save(skill);
            return "redirect:/skills/" + savedSkill.getSkillId();
        }
    }

    @GetMapping("/skills/{skillId}/edit")
    public String initUpdateSkillForm(@PathVariable("skillId") int skillId, Model model) {
        Skill skill = skillRepository.findById(skillId).orElseThrow();
        model.addAttribute(skill);
        return CREATE_OR_UPDATE_SKILL_VIEW;
    }

    @PostMapping("/skills/{skillId}/edit")
    public String processUpdateSkillForm(@Valid Skill skill, BindingResult result,
                                         @PathVariable("skillId") int skillId) {
        if (result.hasErrors()) {
            return CREATE_OR_UPDATE_SKILL_VIEW;
        } else {
            skill.setSkillId(skillId);
            Skill savedSkill = skillRepository.save(skill);
            return "redirect:/skills/" + savedSkill.getSkillId();
        }
    }
}
