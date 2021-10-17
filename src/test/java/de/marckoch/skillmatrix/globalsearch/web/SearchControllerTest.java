package de.marckoch.skillmatrix.globalsearch.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static de.marckoch.skillmatrix.skills.web.ViewNames.DEVELOPER_LIST;
import static de.marckoch.skillmatrix.skills.web.ViewNames.EMPTY_SEARCH;
import static de.marckoch.skillmatrix.skills.web.ViewNames.SKILL_LIST;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(SearchController.class)
class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeveloperRepository developerRepository;

    @MockBean
    private SkillRepository skillRepository;

    @Test
    void searchingDeveloperNameShouldReturnDeveloperList() throws Exception {
        Developer dev1 = new Developer();
        dev1.setLastName("developer1lastName");
        dev1.setExperiences(Collections.emptyList());

        when(developerRepository.findByQuery("dev1".toUpperCase())).thenReturn(List.of(dev1));

        mockMvc.perform(get("/globalsearch").param("query", "dev1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("developers"))
                .andExpect(view().name(DEVELOPER_LIST))
                .andExpect(content().string(containsString(dev1.getLastName())));
    }

    @Test
    void searchingSkillNameShouldReturnSkillList() throws Exception {
        Skill skill1 = new Skill();
        skill1.setName("skillName1");
        skill1.setExperiences(Collections.emptyList());

        when(skillRepository.findByQuery("skillName1".toUpperCase())).thenReturn(List.of(skill1));

        mockMvc.perform(get("/globalsearch").param("query", "skillName1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("skills"))
                .andExpect(view().name(SKILL_LIST))
                .andExpect(content().string(containsString(skill1.getName())));
    }

    @Test
    void noResultShouldReturnEmptySearchPage() throws Exception {
        when(developerRepository.findByQuery(anyString())).thenReturn(Collections.emptyList());
        when(skillRepository.findByQuery(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/globalsearch").param("query", "xxx"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("developers", "skills"))
                .andExpect(view().name(EMPTY_SEARCH))
                .andExpect(content().string(containsString("Nothing found!")));
    }
}