package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import de.marckoch.skillmatrix.skills.service.SkillMatrixService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static de.marckoch.skillmatrix.skills.web.ViewNames.SKILL_MATRIX;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest({SkillMatrixController.class, SkillMatrixService.class})
class SkillMatrixControllerTest {
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
    void skillMatrixWorks() throws Exception {
        dev1.getExperiences().add(expDev1Skill1);
        skill1.getExperiences().add(expDev1Skill1);

        when(skillRepository.findAll()).thenReturn(List.of(skill1, skill2, skill3));
        when(developerRepository.findAll()).thenReturn(List.of(dev1, dev2));
        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));
        when(developerRepository.findById(dev2.getDeveloperId())).thenReturn(Optional.of(dev2));

        mockMvc.perform(get("/skills/matrix"))
                .andExpect(status().isOk())
                .andExpect(view().name(SKILL_MATRIX));
    }
}