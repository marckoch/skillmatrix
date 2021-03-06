package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import de.marckoch.skillmatrix.skills.web.dto.SkillMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILL_DTO;
import static de.marckoch.skillmatrix.skills.web.ViewNames.CREATE_OR_UPDATE_SKILL_VIEW;
import static de.marckoch.skillmatrix.skills.web.ViewNames.REDIRECT_SKILLS;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WithMockUser
@WebMvcTest({SkillEditController.class, SkillMapper.class})
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
                .andExpect(model().attributeExists(SKILL_DTO))
                .andExpect(model().attribute(SKILL_DTO, hasProperty("new", is(true))))
                .andExpect(model().attribute(SKILL_DTO, not(hasProperty("id"))))
                .andExpect(view().name(CREATE_OR_UPDATE_SKILL_VIEW));
    }

    @Test
    void processCreationFormWithWrongDataShouldShowError() throws Exception {
        // error because name is missing in post!
        mockMvc.perform(post("/skills/new").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode(SKILL_DTO, "name", "NotEmpty"))
                .andExpect(view().name(CREATE_OR_UPDATE_SKILL_VIEW));
    }

    @Test
    void processCreationFormWithCorrectDataShouldSaveData() throws Exception {
        Skill newSkill = new Skill();
        newSkill.setSkillId(333);

        when(skillRepository.save(ArgumentMatchers.any(Skill.class))).thenReturn(newSkill);

        mockMvc.perform(post("/skills/new")
                        .param("name", "newTestSkill")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(REDIRECT_SKILLS + "/333"));
    }

    @Test
    void processCreationFormWithCancelShouldCancel() throws Exception {
        mockMvc.perform(post("/skills/new")
                        .param("name", "newSkill")
                        .param("cancel", "true")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(REDIRECT_SKILLS));

        verifyNoInteractions(skillRepository);
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
        mockMvc.perform(post("/skills/123/edit").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode(SKILL_DTO, "name", "NotEmpty"))
                .andExpect(view().name(CREATE_OR_UPDATE_SKILL_VIEW))
                .andReturn();
    }

    @Test
    void processUpdateFormWithCorrectDataShouldSaveData() throws Exception {
        when(skillRepository.findById(1)).thenReturn(Optional.of(skill1));
        when(skillRepository.save(ArgumentMatchers.any(Skill.class))).thenReturn(skill1);

        mockMvc.perform(post("/skills/1/edit")
                        .param("name", "nnn")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(REDIRECT_SKILLS + "/1"));
    }

    @Test
    void processUpdateFormWithCancelShouldCancel() throws Exception {
        mockMvc.perform(post("/skills/1/edit")
                        .param("name", "newSkillName")
                        .param("cancel", "true")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(REDIRECT_SKILLS + "/1"));

        verifyNoInteractions(skillRepository);
    }
}
