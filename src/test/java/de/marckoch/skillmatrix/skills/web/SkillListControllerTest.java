package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.CURRENT_PAGE;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.REVERSE_SORT_DIR;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILLS;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SORT_DIR;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SORT_FIELD;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.TOTAL_ITEMS;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.TOTAL_PAGES;
import static de.marckoch.skillmatrix.skills.web.SortDirection.ASC;
import static de.marckoch.skillmatrix.skills.web.SortDirection.DESC;
import static de.marckoch.skillmatrix.skills.web.SortField.SKILL_NAME;
import static de.marckoch.skillmatrix.skills.web.ViewNames.REDIRECT_SKILLS;
import static de.marckoch.skillmatrix.skills.web.ViewNames.SKILL_LIST;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WithMockUser
@WebMvcTest(SkillListController.class)
class SkillListControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SkillRepository skillRepository;

    Skill skill1 = Skill.builder().skillId(1).name("skill1").experiences(Collections.emptyList()).build();
    Skill skill2 = Skill.builder().skillId(2).name("skill2").experiences(Collections.emptyList()).build();
    Skill skill3 = Skill.builder().skillId(3).name("skill3").experiences(Collections.emptyList()).build();

    @Test
    void showAllInitiallyRedirects() throws Exception {
        mockMvc.perform(get("/skills"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(REDIRECT_SKILLS + "/page/0?sort-field=name&sort-dir=asc"));
    }

    @Test
    void showAllShowsSkillList() throws Exception {
        when(skillRepository.findAllInSkillList(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(skill1, skill2, skill3)));

        mockMvc.perform(get("/skills/page/{pagenumber}", 0)
                        .param(RequestParams.SORT_FIELD, SKILL_NAME)
                        .param(RequestParams.SORT_DIR, ASC))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists(SKILLS))
                .andExpect(model().attribute(SKILLS, instanceOf(PageImpl.class)))
                .andExpect(model().attribute(CURRENT_PAGE, is(0)))
                .andExpect(model().attribute(TOTAL_PAGES, is(1)))
                .andExpect(model().attribute(TOTAL_ITEMS, is(3L)))
                .andExpect(model().attribute(SORT_FIELD, is(SKILL_NAME)))
                .andExpect(model().attribute(SORT_DIR, is(ASC)))
                .andExpect(model().attribute(REVERSE_SORT_DIR, is(DESC)))
                .andExpect(view().name(SKILL_LIST));
    }

    @Test
    void showAllShowsSkillListWithSortingReversed() throws Exception {
        when(skillRepository.findAllInSkillList(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(skill1, skill2, skill3)));

        mockMvc.perform(get("/skills/page/{pagenumber}", 0)
                        .param(RequestParams.SORT_FIELD, SKILL_NAME)
                        .param(RequestParams.SORT_DIR, DESC))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists(SKILLS))
                .andExpect(model().attribute(SKILLS, instanceOf(PageImpl.class)))
                .andExpect(model().attribute(CURRENT_PAGE, is(0)))
                .andExpect(model().attribute(TOTAL_PAGES, is(1)))
                .andExpect(model().attribute(TOTAL_ITEMS, is(3L)))
                .andExpect(model().attribute(SORT_FIELD, is(SKILL_NAME)))
                .andExpect(model().attribute(SORT_DIR, is(DESC)))
                .andExpect(model().attribute(REVERSE_SORT_DIR, is(ASC)))
                .andExpect(view().name(SKILL_LIST));
    }
}
