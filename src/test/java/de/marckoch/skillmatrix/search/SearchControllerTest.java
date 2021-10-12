package de.marckoch.skillmatrix.search;

import de.marckoch.skillmatrix.skills.Developer;
import de.marckoch.skillmatrix.skills.DeveloperRepository;
import de.marckoch.skillmatrix.skills.Skill;
import de.marckoch.skillmatrix.skills.SkillRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeveloperRepository developerRepository;

    @MockBean
    private SkillRepository skillRepository;

    @Test
    public void searchingDeveloperNameShouldReturnDeveloperList() throws Exception {
        Developer dev1 = new Developer();
        dev1.setLastName("developer1lastName");
        dev1.setExperiences(Collections.emptyList());

        when(developerRepository.findByQuery("dev1".toUpperCase())).thenReturn(List.of(dev1));

        this.mockMvc.perform(get("/search").param("query", "dev1"))
                .andExpect(status().isOk())
                .andExpect(view().name("developers/developerList"))
                .andExpect(content().string(containsString(dev1.getLastName())));
    }

    @Test
    public void searchingSkillNameShouldReturnSkillList() throws Exception {
        Skill skill1 = new Skill();
        skill1.setName("skillName1");
        skill1.setExperiences(Collections.emptyList());

        when(skillRepository.findByQuery("skillName1".toUpperCase())).thenReturn(List.of(skill1));

        this.mockMvc.perform(get("/search").param("query", "skillName1"))
                .andExpect(status().isOk())
                .andExpect(view().name("skills/skillList"))
                .andExpect(content().string(containsString(skill1.getName())));
    }

    @Test
    public void noResultShouldReturnEmptySearchPage() throws Exception {
        when(developerRepository.findByQuery(anyString())).thenReturn(Collections.emptyList());
        when(skillRepository.findByQuery(anyString())).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get("/search").param("query", "xxx"))
                .andExpect(status().isOk())
                .andExpect(view().name("search/emptySearch"))
                .andExpect(content().string(containsString("Nothing found!")));
    }
}