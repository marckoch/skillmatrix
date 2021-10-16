package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(SkillDetailsController.class)
class SkillDetailsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SkillRepository skillRepository;

    Skill skill1 = Skill.builder().skillId(1).name("skill1").experiences(Collections.emptyList()).build();

    @Test
    void showSkillWithExistingSkillShowsSkillDetails() throws Exception {
        when(skillRepository.findById(skill1.getSkillId())).thenReturn(Optional.of(skill1));

        mockMvc.perform(get("/skills/{skillId}", skill1.getSkillId()))
                .andExpect(status().isOk())
                .andExpect(view().name("skills/skillDetails"));
    }

    @Test
    void showSkillWithNoneExistingSkillShowsSkillDetails() throws Exception {
        when(skillRepository.findById(skill1.getSkillId())).thenReturn(Optional.empty());

        mockMvc.perform(get("/skills/{skillId}", skill1.getSkillId()))
                .andExpect(status().isNotFound());
    }
}
