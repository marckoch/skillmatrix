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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILLS;
import static de.marckoch.skillmatrix.skills.web.RequestParams.SORT_DIR;
import static de.marckoch.skillmatrix.skills.web.RequestParams.SORT_FIELD;
import static de.marckoch.skillmatrix.skills.web.SortDirection.ASC;
import static de.marckoch.skillmatrix.skills.web.SortField.SKILL_NAME;
import static de.marckoch.skillmatrix.skills.web.ViewNames.REDIRECT_SKILLS;
import static de.marckoch.skillmatrix.skills.web.ViewNames.SKILL_LIST;

@Controller
@AllArgsConstructor
class SkillListController {

    private final SkillRepository skillRepository;

    @GetMapping("/skills")
    public String showAll() {
        return REDIRECT_SKILLS + "/page/0?sort-field=name&sort-dir=asc";
    }

    @GetMapping("/skills/page/{pagenumber}")
    public String showAll(@PathVariable int pagenumber,
                          @RequestParam(name = SORT_FIELD, defaultValue = SKILL_NAME) final String sortField,
                          @RequestParam(name = SORT_DIR, defaultValue = ASC) final String sortDir,
                          Model model) {
        Sort sort = SortUtil.build(sortDir, sortField);
        Pageable p = PageRequest.of(pagenumber, 10, sort);
        Page<Skill> resultPage = skillRepository.findAllInSkillList(p);

        model.addAttribute(SKILLS, resultPage);

        PaginationUtil.addPagingAttributesToModel(model, resultPage, pagenumber);
        SortUtil.addSortAttributesToModel(model, sortField, sortDir);

        return SKILL_LIST;
    }
}
