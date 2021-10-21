package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import de.marckoch.skillmatrix.skills.service.SkillsService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static de.marckoch.skillmatrix.skills.web.DeveloperEditController.REDIRECT_DEVELOPERS;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.DEVELOPER_DTO;
import static de.marckoch.skillmatrix.skills.web.ViewNames.CREATE_OR_UPDATE_DEVELOPER_VIEW;
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
        mockMvc.perform(post("/developers/new"))
                .andExpect(status().isOk())
                .andExpect(model().errorCount(2))
                .andExpect(model().attributeHasFieldErrorCode(DEVELOPER_DTO, "firstName", "NotEmpty"))
                .andExpect(model().attributeHasFieldErrorCode(DEVELOPER_DTO, "lastName", "NotEmpty"))
                .andExpect(view().name(CREATE_OR_UPDATE_DEVELOPER_VIEW));
    }

    @Test
    void processCreationFormWithCorrectDataShouldSaveData() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);

        when(developerRepository.save(ArgumentMatchers.any(Developer.class))).thenReturn(dev1);

        mockMvc.perform(post("/developers/new")
                        .param("firstName", "first")
                        .param("lastName", "last"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(REDIRECT_DEVELOPERS + "123"));
    }

    @Test
    void initUpdateDeveloperFormShouldShowExistingDeveloper() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);
        dev1.setFirstName("firstName123");
        dev1.setLastName("lastName123");

        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));

        mockMvc.perform(get("/developers/{developerId}/edit", 123))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(CREATE_OR_UPDATE_DEVELOPER_VIEW))
                .andExpect(content().string(containsString(dev1.getFirstName())))
                .andExpect(content().string(containsString(dev1.getLastName())));
    }

    @Test
    void processUpdateFormWithWrongDataShouldShowError() throws Exception {
        // error because first and last name is missing in post!
        mockMvc.perform(post("/developers/123/edit"))
                .andExpect(status().isOk())
                .andExpect(model().errorCount(2))
                .andExpect(model().attributeHasFieldErrorCode(DEVELOPER_DTO, "firstName", "NotEmpty"))
                .andExpect(model().attributeHasFieldErrorCode(DEVELOPER_DTO, "lastName", "NotEmpty"))
                .andExpect(view().name(CREATE_OR_UPDATE_DEVELOPER_VIEW));
    }

    @Test
    void processUpdateFormWithCorrectDataShouldSaveData() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);

        when(developerRepository.findById(123)).thenReturn(Optional.of(dev1));
        when(developerRepository.save(ArgumentMatchers.any(Developer.class))).thenReturn(dev1);

        mockMvc.perform(post("/developers/123/edit")
                        .param("firstName", "first")
                        .param("lastName", "last"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(REDIRECT_DEVELOPERS + "123"));
    }
}