package de.marckoch.skillmatrix.skills.rest;

import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SkillsRestController.class)
class SkillsRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SkillRepository skillRepository;

    Skill skill1 = Skill.builder().skillId(1).name("skill1").build();
    Skill skill2 = Skill.builder().skillId(2).name("skill2").build();
    Skill skill3 = Skill.builder().skillId(3).name("skill3").build();

    @Test
    void processAddSkillToDeveloperFormShouldRedirectWithErrorsInFlashAttributes() throws Exception {

        when(skillRepository.findByQuery("S")).thenReturn(List.of(skill1, skill2, skill3));

        mockMvc.perform(get("/skills/json").param("q", "s"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(3)))
                .andExpect(content().string(containsString("\"name\":\"skill1\"")))
                .andExpect(content().string(containsString("\"name\":\"skill2\"")))
                .andExpect(content().string(containsString("\"name\":\"skill3\"")))
                .andExpect(content().string(containsString("\"id\":\"skill1\"")))
                .andExpect(content().string(containsString("\"id\":\"skill2\"")))
                .andExpect(content().string(containsString("\"id\":\"skill3\"")));
    }
}