package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import de.marckoch.skillmatrix.skills.service.SkillsService;
import de.marckoch.skillmatrix.skills.web.dto.DeveloperMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.DEVELOPER_DTO;
import static de.marckoch.skillmatrix.skills.web.ViewNames.CREATE_OR_UPDATE_DEVELOPER_VIEW;
import static de.marckoch.skillmatrix.skills.web.ViewNames.REDIRECT_DEVELOPERS;
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
@WebMvcTest({DeveloperEditController.class, DeveloperMapper.class})
class DeveloperEditControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    DeveloperRepository developerRepository;

    @MockBean
    SkillRepository skillRepository;

    @MockBean
    SkillsService skillsService;

    Developer dev1 = Developer.builder()
            .developerId(123)
            .firstName("firstName123")
            .lastName("lastName123")
            .build();

    @Test
    void initCreationFormShouldShowNewDeveloper() throws Exception {
        mockMvc.perform(get("/developers/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(DEVELOPER_DTO))
                .andExpect(model().attribute(DEVELOPER_DTO, hasProperty("new", is(true))))
                .andExpect(model().attribute(DEVELOPER_DTO, not(hasProperty("id"))))
                .andExpect(view().name(CREATE_OR_UPDATE_DEVELOPER_VIEW));
    }

    @Test
    void processCreationFormWithWrongDataShouldShowError() throws Exception {
        // error because first and last name is missing in post!
        mockMvc.perform(post("/developers/new").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().errorCount(2))
                .andExpect(model().attributeHasFieldErrorCode(DEVELOPER_DTO, "firstName", "NotEmpty"))
                .andExpect(model().attributeHasFieldErrorCode(DEVELOPER_DTO, "lastName", "NotEmpty"))
                .andExpect(view().name(CREATE_OR_UPDATE_DEVELOPER_VIEW));
    }

    @Test
    void processCreationFormWithCorrectDataShouldSaveData() throws Exception {
        when(developerRepository.save(ArgumentMatchers.any(Developer.class))).thenReturn(dev1);

        mockMvc.perform(post("/developers/new")
                        .param("firstName", "first")
                        .param("lastName", "last")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(REDIRECT_DEVELOPERS + "/123"));
    }

    @Test
    void processCreationFormWithCancelShouldCancel() throws Exception {
        mockMvc.perform(post("/developers/new")
                        .param("firstName", "first")
                        .param("lastName", "last")
                        .param("cancel", "true")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(REDIRECT_DEVELOPERS));

        verifyNoInteractions(developerRepository);
    }

    @Test
    void initUpdateDeveloperFormShouldShowExistingDeveloper() throws Exception {
        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));

        mockMvc.perform(get("/developers/{developerId}/edit", dev1.getDeveloperId()))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(CREATE_OR_UPDATE_DEVELOPER_VIEW))
                .andExpect(content().string(containsString(dev1.getFirstName())))
                .andExpect(content().string(containsString(dev1.getLastName())));
    }

    @Test
    void processUpdateFormWithWrongDataShouldShowError() throws Exception {
        // error because first and last name is missing in post!
        mockMvc.perform(post("/developers/123/edit").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().errorCount(2))
                .andExpect(model().attributeHasFieldErrorCode(DEVELOPER_DTO, "firstName", "NotEmpty"))
                .andExpect(model().attributeHasFieldErrorCode(DEVELOPER_DTO, "lastName", "NotEmpty"))
                .andExpect(view().name(CREATE_OR_UPDATE_DEVELOPER_VIEW));
    }

    @Test
    void processUpdateFormWithCorrectDataShouldSaveData() throws Exception {
        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));
        when(developerRepository.save(ArgumentMatchers.any(Developer.class))).thenReturn(dev1);

        mockMvc.perform(post("/developers/123/edit")
                        .param("firstName", "first")
                        .param("lastName", "last")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(REDIRECT_DEVELOPERS + "/123"));
    }

    @Test
    void processUpdateFormWithCancelShouldCancel() throws Exception {
        mockMvc.perform(post("/developers/123/edit")
                        .param("firstName", "first")
                        .param("lastName", "last")
                        .param("cancel", "true")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(REDIRECT_DEVELOPERS + "/123"));

        verifyNoInteractions(developerRepository);
    }
}