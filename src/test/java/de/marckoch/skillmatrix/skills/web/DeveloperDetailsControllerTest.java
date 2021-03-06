package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import de.marckoch.skillmatrix.skills.service.SkillsService;
import de.marckoch.skillmatrix.skills.web.dto.ExperienceDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.DEVELOPER;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.EXPERIENCE_DTO;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SKILL_SELECT_ITEMS;
import static de.marckoch.skillmatrix.skills.web.ViewNames.DEVELOPER_DETAILS;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WithMockUser
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
        Skill skill1 = Skill.builder().skillId(1).name("skill1").experiences(Collections.emptyList()).build();
        Skill skill2 = Skill.builder().skillId(2).name("skill2").experiences(Collections.emptyList()).build();

        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);
        dev1.setLastName("developer1lastName");
        dev1.setExperiences(Collections.emptyList());

        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));

        when(skillsService.getFreeSkills(dev1)).thenReturn(List.of(skill1, skill2));

        mockMvc.perform(get("/developers/{developerId}", dev1.getDeveloperId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(DEVELOPER, EXPERIENCE_DTO, SKILL_SELECT_ITEMS))
                .andExpect(model().attribute(SKILL_SELECT_ITEMS, Matchers.hasSize(2)))
                .andExpect(view().name(DEVELOPER_DETAILS))
                .andExpect(content().string(containsString(dev1.getLastName())));
    }

    @Test
    void showDeveloperThrowsExceptionForUnknownDeveloper() throws Exception {
        when(developerRepository.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/developers/{developerId}", 123))
                .andExpect(status().isNotFound());
    }

    @Test
    void redirectionFromExperienceCheckHasDTOinFlashAttributes() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);
        dev1.setExperiences(Collections.emptyList());

        ExperienceDTO expDTO = new ExperienceDTO();

        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));

        mockMvc.perform(get("/developers/{developerId}", dev1.getDeveloperId())
                        .flashAttr(EXPERIENCE_DTO, expDTO))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(DEVELOPER, EXPERIENCE_DTO, SKILL_SELECT_ITEMS))
                .andExpect(model().attribute(EXPERIENCE_DTO, Matchers.equalTo(expDTO)))
                .andExpect(view().name(DEVELOPER_DETAILS));
    }
}