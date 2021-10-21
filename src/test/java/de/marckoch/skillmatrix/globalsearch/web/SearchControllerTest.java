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
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.DEVELOPERS;
import static de.marckoch.skillmatrix.skills.web.ViewNames.EMPTY_SEARCH;
import static de.marckoch.skillmatrix.skills.web.ViewNames.SEARCH_RESULT;
import static org.assertj.core.api.Assertions.assertThat;
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
        dev1.setFirstName("firstNameDev1xx");
        dev1.setLastName("lastNameDev1xx");
        dev1.setExperiences(Collections.emptyList());

        when(developerRepository.findByQuery("Dev1".toUpperCase())).thenReturn(List.of(dev1));

        MvcResult result = mockMvc.perform(get("/globalsearch").param("query", "dev1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(DEVELOPERS))
                .andExpect(view().name(SEARCH_RESULT))
                .andExpect(content().string(containsString(dev1.getLastName())))
                .andReturn();

        @SuppressWarnings("unchecked")
        List<Developer> devs = (List<Developer>) Objects.requireNonNull(result.getModelAndView()).getModel().get("developers");
        assertThat(devs.get(0).getLastName()).isEqualTo("lastName<mark>Dev1</mark>xx");
    }

    @Test
    void searchingSkillNameShouldReturnSkillList() throws Exception {
        Skill skill1 = new Skill();
        skill1.setName("xxskillName1xx");
        skill1.setAlias("xAlliasxskillName1xx");
        skill1.setExperiences(Collections.emptyList());

        when(skillRepository.findByQuery("skillName1".toUpperCase())).thenReturn(List.of(skill1));

        MvcResult result = mockMvc.perform(get("/globalsearch").param("query", "skillName1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("skills"))
                .andExpect(view().name(SEARCH_RESULT))
                .andExpect(content().string(containsString(skill1.getName())))
                .andReturn();

        @SuppressWarnings("unchecked")
        List<Skill> skills = (List<Skill>) Objects.requireNonNull(result.getModelAndView()).getModel().get("skills");
        assertThat(skills.get(0).getName()).isEqualTo("xx<mark>skillName1</mark>xx");
    }

    @Test
    void noResultShouldReturnEmptySearchPage() throws Exception {
        when(developerRepository.findByQuery(anyString())).thenReturn(Collections.emptyList());
        when(skillRepository.findByQuery(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/globalsearch").param("query", "xxx"))
                .andExpect(status().isOk())
                .andExpect(view().name(EMPTY_SEARCH))
                .andExpect(content().string(containsString("Nothing found!")));
    }
}