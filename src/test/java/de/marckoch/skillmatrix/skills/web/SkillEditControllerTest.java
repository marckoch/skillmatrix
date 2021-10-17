package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Collections;
import java.util.Optional;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILL_DTO;
import static de.marckoch.skillmatrix.skills.web.SkillEditController.CREATE_OR_UPDATE_SKILL_VIEW;
import static de.marckoch.skillmatrix.skills.web.SkillEditController.REDIRECT_SKILLS;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(SkillEditController.class)
class SkillEditControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SkillRepository skillRepository;

    Skill skill1 = Skill.builder().skillId(1).name("skill1").experiences(Collections.emptyList()).build();

    @Test
    void initCreationFormShouldShowNewDeveloper() throws Exception {
        mockMvc.perform(get("/skills/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(SKILL_DTO.modelAttributeName))
                .andExpect(model().attribute(SKILL_DTO.modelAttributeName, hasProperty("new", is(true))))
                .andExpect(model().attribute(SKILL_DTO.modelAttributeName, not(hasProperty("id"))))
                .andExpect(view().name(CREATE_OR_UPDATE_SKILL_VIEW));
    }

    @Test
    void processCreationFormWithWrongDataShouldShowError() throws Exception {
        // error because name is missing in post!
        mockMvc.perform(post("/skills/new"))
                .andExpect(status().isOk())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode(SKILL_DTO.modelAttributeName, "name", "NotEmpty"))
                .andExpect(view().name(CREATE_OR_UPDATE_SKILL_VIEW));
    }

    @Test
    void processCreationFormWithCorrectDataShouldSaveData() throws Exception {
        Skill newSkill = new Skill();
        newSkill.setSkillId(333);

        when(skillRepository.save(ArgumentMatchers.any(Skill.class))).thenReturn(newSkill);

        mockMvc.perform(post("/skills/new")
                        .param("name", "newTestSkill"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(REDIRECT_SKILLS + "333"));
    }

    @Test
    void initUpdateSkillFormShouldShowExistingSkill() throws Exception {
        final String newNameForSkill1 = "new name for skill 1";
        skill1.setName(newNameForSkill1);

        when(skillRepository.findById(skill1.getSkillId())).thenReturn(Optional.of(skill1));

        mockMvc.perform(get("/skills/{skillId}/edit", skill1.getSkillId()))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(CREATE_OR_UPDATE_SKILL_VIEW))
                .andExpect(content().string(containsString(newNameForSkill1)));
    }

    @Test
    void processUpdateFormWithWrongDataShouldShowError() throws Exception {
        // error because name is missing in post!
        MvcResult result = mockMvc.perform(post("/skills/123/edit"))
                .andExpect(status().isOk())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode(SKILL_DTO.modelAttributeName, "name", "NotEmpty"))
                .andExpect(view().name(CREATE_OR_UPDATE_SKILL_VIEW))
                .andReturn();
    }

    @Test
    void processUpdateFormWithCorrectDataShouldSaveData() throws Exception {
        when(skillRepository.findById(1)).thenReturn(Optional.of(skill1));
        when(skillRepository.save(ArgumentMatchers.any(Skill.class))).thenReturn(skill1);

        mockMvc.perform(post("/skills/1/edit")
                        .param("name", "nnn"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(REDIRECT_SKILLS + "1"));
    }
}
