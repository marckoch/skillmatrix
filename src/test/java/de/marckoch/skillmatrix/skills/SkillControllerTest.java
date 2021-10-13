package de.marckoch.skillmatrix.skills;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(SkillController.class)
public class SkillControllerTest {
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
                .andExpect(view().name("redirect:skills/page/0?sort-field=name&sort-dir=asc"));
    }

    @Test
    void showAllShowsSkillList() throws Exception {
        when(skillRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(skill1, skill2, skill3)));

        mockMvc.perform(get("/skills/page/{pagenumber}", 0)
                        .param("sort-field", "name")
                        .param("sort-dir", "asc"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("skills"))
                .andExpect(model().attribute("skills", instanceOf(PageImpl.class)))
                .andExpect(model().attribute("currentPage", is(0)))
                .andExpect(model().attribute("totalPages", is(1)))
                .andExpect(model().attribute("totalItems", is(3L)))
                .andExpect(model().attribute("sortField", is("name")))
                .andExpect(model().attribute("sortDir", is("asc")))
                .andExpect(model().attribute("reverseSortDir", is("desc")))
                .andExpect(view().name("skills/skillList"));
    }

    @Test
    void showAllShowsSkillListWithSortingReversed() throws Exception {
        when(skillRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(skill1, skill2, skill3)));

        mockMvc.perform(get("/skills/page/{pagenumber}", 0)
                        .param("sort-field", "name")
                        .param("sort-dir", "desc"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("skills"))
                .andExpect(model().attribute("skills", instanceOf(PageImpl.class)))
                .andExpect(model().attribute("currentPage", is(0)))
                .andExpect(model().attribute("totalPages", is(1)))
                .andExpect(model().attribute("totalItems", is(3L)))
                .andExpect(model().attribute("sortField", is("name")))
                .andExpect(model().attribute("sortDir", is("desc")))
                .andExpect(model().attribute("reverseSortDir", is("asc")))
                .andExpect(view().name("skills/skillList"));
    }

    @Test
    void showSkillWithExistingSkillShowsSkillDetails() throws Exception {
        when(skillRepository.findById(skill1.getSkillId())).thenReturn(Optional.of(skill1));

        mockMvc.perform(get("/skills/{skillId}", skill1.getSkillId()))
                .andExpect(status().isOk())
                .andExpect(view().name("skills/skillDetails"));
    }

    @Test
    void showSkillWithNoneExistingSkillShowsSkillDetails() throws Exception {
        when(skillRepository.findById(skill1.getSkillId())).thenReturn(Optional.empty());

        mockMvc.perform(get("/skills/{skillId}", skill1.getSkillId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void initCreationFormShouldShowNewDeveloper() throws Exception {
        mockMvc.perform(get("/skills/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("skill"))
                .andExpect(model().attribute("skill", hasProperty("new", is(true))))
                .andExpect(model().attribute("skill", not(hasProperty("id"))))
                .andExpect(view().name("/skills/createOrUpdateSkillForm"));
    }

    @Test
    void processCreationFormWithWrongDataShouldShowError() throws Exception {
        // error because first and last name is missing in post!
        mockMvc.perform(post("/skills/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(view().name("/skills/createOrUpdateSkillForm"));
    }

    @Test
    void processCreationFormWithCorrectDataShouldSaveData() throws Exception {
        Skill newSkill = new Skill();
        newSkill.setSkillId(333);

        when(skillRepository.save(ArgumentMatchers.any(Skill.class))).thenReturn(newSkill);

        mockMvc.perform(post("/skills/new")
                        .param("name", "newTestSkill"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/skills/333"));
    }

    @Test
    void initUpdateSkillFormShouldShowExistingSkill() throws Exception {
        final String newNameForSkill1 = "new name for skill 1";
        skill1.setName(newNameForSkill1);

        when(skillRepository.findById(skill1.getSkillId())).thenReturn(Optional.of(skill1));

        mockMvc.perform(get("/skills/{skillId}/edit", skill1.getSkillId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("/skills/createOrUpdateSkillForm"))
                .andExpect(content().string(containsString(newNameForSkill1)));
    }

    @Test
    void processUpdateFormWithWrongDataShouldShowError() throws Exception {
        // error because name is missing in post!
        mockMvc.perform(post("/skills/123/edit"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(view().name("/skills/createOrUpdateSkillForm"));
    }

    @Test
    void processUpdateFormWithCorrectDataShouldSaveData() throws Exception {
        when(skillRepository.save(ArgumentMatchers.any(Skill.class))).thenReturn(skill1);

        mockMvc.perform(post("/skills/1/edit")
                        .param("name", "nnn"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/skills/1"));
    }
}
