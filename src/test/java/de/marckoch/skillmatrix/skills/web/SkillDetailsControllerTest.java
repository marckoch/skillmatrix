package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import de.marckoch.skillmatrix.skills.web.dto.ExperienceMapper;
import de.marckoch.skillmatrix.skills.web.dto.SkillMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static de.marckoch.skillmatrix.skills.web.ViewNames.SKILL_DETAILS;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WithMockUser
@WebMvcTest({SkillDetailsController.class, SkillMapper.class, ExperienceMapper.class})
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
                .andExpect(view().name(SKILL_DETAILS));
    }

    @Test
    void showSkillWithNoneExistingSkillShowsSkillDetails() throws Exception {
        when(skillRepository.findById(skill1.getSkillId())).thenReturn(Optional.empty());

        mockMvc.perform(get("/skills/{skillId}", skill1.getSkillId()))
                .andExpect(status().isNotFound());
    }
}
