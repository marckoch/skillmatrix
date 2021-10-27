package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.ExperienceRepository;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.web.dto.ExperienceDTO;
import de.marckoch.skillmatrix.skills.web.dto.ExperienceMapper;
import de.marckoch.skillmatrix.skills.web.dto.SelectItem;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static de.marckoch.skillmatrix.skills.web.DeveloperEditController.REDIRECT_DEVELOPERS;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.DEVELOPER;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.EXPERIENCE_DTO;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILL_SELECT_ITEMS;
import static de.marckoch.skillmatrix.skills.web.ViewNames.EXPERIENCE_EDIT_VIEW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WithMockUser
@WebMvcTest({ExperienceEditController.class, ExperienceMapper.class})
class ExperienceEditControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ExperienceRepository experienceRepository;

    @MockBean
    DeveloperRepository developerRepository;

    Skill skill1 = Skill.builder().skillId(1).name("skill1").experiences(Collections.emptyList()).build();

    Developer dev1 = Developer.builder()
            .developerId(123)
            .experiences(new ArrayList<>())
            .build();

    Experience exp1 = Experience.builder()
            .developer(dev1)
            .skill(skill1)
            .experienceId(123)
            .rating(5)
            .years(12)
            .build();

    @Test
    void processAddSkillToDeveloperFormShouldRedirectWithErrorsInFlashAttributes() throws Exception {
        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));

        // error because years and rating are missing in post
        MvcResult result = mockMvc.perform(post("/experience/{developerId}/new", 123)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attributeExists(EXPERIENCE_DTO, "org.springframework.validation.BindingResult.experienceDTO"))
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
        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));

        // error because years and rating are wrong values
        MvcResult result = mockMvc.perform(post("/experience/{developerId}/new", 123)
                        .param("years", "99")
                        .param("rating", "8")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attributeExists(EXPERIENCE_DTO, "org.springframework.validation.BindingResult.experienceDTO"))
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
        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));

        mockMvc.perform(post("/experience/{developerId}/new", 123)
                        .param("years", "5")
                        .param("rating", "4")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/developers/123"));

        verify(experienceRepository, times(1)).save(any(Experience.class));
    }

    @Test
    void deletingExistingExperienceWorks() throws Exception {
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

    @Test
    void initUpdateExperienceFormShouldShowExistingExperience() throws Exception {
        when(experienceRepository.findById(exp1.getExperienceId())).thenReturn(Optional.of(exp1));

        MvcResult result = mockMvc.perform(get("/experience/edit/{experienceId}", exp1.getExperienceId()))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(EXPERIENCE_EDIT_VIEW))
                .andExpect(model().attributeExists(EXPERIENCE_DTO, DEVELOPER, SKILL_SELECT_ITEMS))
                .andReturn();

        Object expObj = Objects.requireNonNull(result.getModelAndView()).getModel().get(EXPERIENCE_DTO);
        assertThat(expObj).isInstanceOf(ExperienceDTO.class);
        ExperienceDTO experienceDTO = (ExperienceDTO) expObj;

        assertThat(experienceDTO.getExperienceId()).isEqualTo(exp1.getExperienceId());
        assertThat(experienceDTO.getDeveloper()).isEqualTo(dev1);

        Object selectItemsObj = Objects.requireNonNull(result.getModelAndView()).getModel().get(SKILL_SELECT_ITEMS);
        assertThat(selectItemsObj).isInstanceOf(List.class);
        @SuppressWarnings("unchecked")
        List<SelectItem> selectItems = (List<SelectItem>) selectItemsObj;
        assertThat(selectItems).hasSize(1);
        assertThat(selectItems.get(0).getKey()).isEqualTo(skill1.getSkillId());
    }

    @Test
    void processUpdateFormWithWrongDataShouldShowError() throws Exception {
        when(experienceRepository.findById(exp1.getExperienceId())).thenReturn(Optional.of(exp1));

        // error because years is too big!
        mockMvc.perform(post("/experience/edit/123")
                        .param("years", "123")
                        .param("rating", "3")
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode(EXPERIENCE_DTO, "years", "Max"))
                .andExpect(model().attributeExists(EXPERIENCE_DTO, DEVELOPER, SKILL_SELECT_ITEMS))
                .andExpect(view().name(EXPERIENCE_EDIT_VIEW));
    }


    @Test
    void processUpdateFormWithCorrectDataShouldSaveData() throws Exception {
        when(experienceRepository.findById(exp1.getExperienceId())).thenReturn(Optional.of(exp1));

        when(experienceRepository.save(ArgumentMatchers.any(Experience.class))).thenReturn(exp1);

        mockMvc.perform(post("/experience/edit/123")
                        .param("rating", "2")
                        .param("years", "22")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(REDIRECT_DEVELOPERS + "123"));
    }
}