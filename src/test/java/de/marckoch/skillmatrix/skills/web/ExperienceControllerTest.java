package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.ExperienceRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Optional;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.EXPERIENCE_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ExperienceController.class)
class ExperienceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ExperienceRepository experienceRepository;

    @MockBean
    DeveloperRepository developerRepository;

    @Test
    void processAddSkillToDeveloperFormShouldRedirectWithErrorsInFlashAttributes() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);

        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));

        // error because years and rating are missing in post
        MvcResult result = mockMvc.perform(post("/experience/{developerId}/new", 123))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attributeExists(EXPERIENCE_DTO.modelAttributeName, "org.springframework.validation.BindingResult.experienceDTO"))
                .andExpect(view().name("redirect:/developers/123"))
                .andReturn();

        BindingResult bindingResult = (BindingResult) result.getFlashMap().get("org.springframework.validation.BindingResult.experienceDTO");
        assertThat(bindingResult.getFieldErrorCount()).isEqualTo(2);
        assertThat(bindingResult.getFieldErrors())
                .extracting(fieldError ->
                        new Tuple(fieldError.getField(), fieldError.getDefaultMessage()))
                .contains(new Tuple("years", "must not be null"),
                          new Tuple("rating", "must not be null"));

        verify(experienceRepository, never()).save(any(Experience.class));
    }

    @Test
    void processAddSkillToDeveloperFormShouldRedirectWithWrongData() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);

        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));

        // error because years and rating are wrong values
        MvcResult result = mockMvc.perform(post("/experience/{developerId}/new", 123)
                        .param("years", "99")
                        .param("rating", "8"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attributeExists(EXPERIENCE_DTO.modelAttributeName, "org.springframework.validation.BindingResult.experienceDTO"))
                .andExpect(view().name("redirect:/developers/123"))
                .andReturn();

        BindingResult bindingResult = (BindingResult) result.getFlashMap().get("org.springframework.validation.BindingResult.experienceDTO");
        assertThat(bindingResult.getFieldErrorCount()).isEqualTo(2);
        assertThat(bindingResult.getFieldErrors())
                .extracting(fieldError ->
                        new Tuple(fieldError.getField(), fieldError.getDefaultMessage()))
                .contains(new Tuple("years", "must be less than or equal to 50"),
                        new Tuple("rating", "must be between 1 and 5"));

        verify(experienceRepository, never()).save(any(Experience.class));
    }

    @Test
    void processAddSkillToDeveloperFormShouldSaveData() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);

        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));

        mockMvc.perform(post("/experience/{developerId}/new", 123)
                        .param("years", "5")
                        .param("rating", "4"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/developers/123"));

        verify(experienceRepository, times(1)).save(any(Experience.class));
    }


    @Test
    void deletingExistingExperienceWorks() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);
        dev1.setExperiences(new ArrayList<>());

        Experience exp1 = new Experience();
        exp1.setExperienceId(111);
        exp1.setDeveloper(dev1);
        dev1.getExperiences().add(exp1);

        when(experienceRepository.findById(exp1.getExperienceId())).thenReturn(Optional.of(exp1));

        mockMvc.perform(get("/experience/delete/{experienceId}", exp1.getExperienceId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/developers/123"));
        verify(developerRepository, times(1)).save(dev1);
        assertThat(dev1.getExperiences()).isEmpty();
    }


    @Test
    void deletingNoneExistingExperienceThrowsError() throws Exception {
        mockMvc.perform(get("/experience/delete/{experienceId}", "1111"))
                .andExpect(status().isNotFound());

        verify(developerRepository, never()).save(any(Developer.class));
    }
}