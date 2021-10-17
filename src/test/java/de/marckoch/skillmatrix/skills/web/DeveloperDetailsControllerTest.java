package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import de.marckoch.skillmatrix.skills.service.SkillsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.DEVELOPER;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.EXPERIENCE;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(DeveloperDetailsController.class)
class DeveloperDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    DeveloperRepository developerRepository;

    @MockBean
    SkillRepository skillRepository;

    @MockBean
    SkillsService skillsService;

    @Test
    void showDeveloperShouldReturnDeveloperDetails() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);
        dev1.setLastName("developer1lastName");
        dev1.setExperiences(Collections.emptyList());

        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));

        mockMvc.perform(get("/developers/{developerId}", dev1.getDeveloperId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(DEVELOPER.modelAttributeName(), EXPERIENCE.modelAttributeName()))
                .andExpect(view().name("developers/developerDetails"))
                .andExpect(content().string(containsString(dev1.getLastName())));
    }

    @Test
    void showDeveloperThrowsExceptionForUnknownDeveloper() throws Exception {
        when(developerRepository.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/developers/{developerId}", 123))
                .andExpect(status().isNotFound());
    }
}