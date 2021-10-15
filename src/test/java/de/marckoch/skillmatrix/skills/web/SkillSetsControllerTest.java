package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(SkillSetsController.class)
class SkillSetsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    DeveloperRepository developerRepository;

    @MockBean
    SkillRepository skillRepository;

    Skill skill1 = Skill.builder().skillId(1).name("skill1").experiences(new ArrayList<>()).build();
    Skill skill2 = Skill.builder().skillId(2).name("skill2").experiences(new ArrayList<>()).build();
    Skill skill3 = Skill.builder().skillId(3).name("skill3").experiences(new ArrayList<>()).build();

    Developer dev1 = Developer.builder().developerId(1).firstName("fn1").lastName("ln1").experiences(new ArrayList<>()).build();
    Developer dev2 = Developer.builder().developerId(2).firstName("fn2").lastName("ln2").experiences(new ArrayList<>()).build();

    Experience expDev1Skill1 = Experience.builder().experienceId(1).developer(dev1).skill(skill1).years(3).rating(5).build();

    @Test
    void skillSetsWorksWithNoQueryParam() throws Exception {
        dev1.getExperiences().add(expDev1Skill1);
        skill1.getExperiences().add(expDev1Skill1);

        when(skillRepository.findAll()).thenReturn(List.of(skill1, skill2, skill3));
        when(developerRepository.findAll()).thenReturn(List.of(dev1, dev2));
        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));
        when(developerRepository.findById(dev2.getDeveloperId())).thenReturn(Optional.of(dev2));

        mockMvc.perform(get("/skills/sets"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("skills", Matchers.hasSize(0)))
                .andExpect(view().name("/skills/skillSets"));
    }

    @Test
    void skillSetsWorksWithOneQueryParam() throws Exception {
        dev1.getExperiences().add(expDev1Skill1);
        skill1.getExperiences().add(expDev1Skill1);

        when(skillRepository.findByQuery("SKILL1")).thenReturn(List.of(skill1));
        when(developerRepository.findAll()).thenReturn(List.of(dev1, dev2));
        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));
        when(developerRepository.findById(dev2.getDeveloperId())).thenReturn(Optional.of(dev2));

        mockMvc.perform(get("/skills/sets").param("skillSetQuery", "skill1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("skills", Matchers.hasSize(1)))
                .andExpect(view().name("/skills/skillSets"));
    }

    @Test
    void skillSetsWorksWithMultipleQueryParams() throws Exception {
        dev1.getExperiences().add(expDev1Skill1);
        skill1.getExperiences().add(expDev1Skill1);

        when(skillRepository.findByQuery("SKILL1")).thenReturn(List.of(skill1));
        when(skillRepository.findByQuery("SKILL2")).thenReturn(List.of(skill2));
        when(developerRepository.findAll()).thenReturn(List.of(dev1, dev2));
        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));
        when(developerRepository.findById(dev2.getDeveloperId())).thenReturn(Optional.of(dev2));

        mockMvc.perform(get("/skills/sets").param("skillSetQuery", "skill1,skill2"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("skills", Matchers.hasSize(2)))
                .andExpect(view().name("/skills/skillSets"));
    }
}