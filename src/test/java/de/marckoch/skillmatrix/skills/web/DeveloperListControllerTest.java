package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import de.marckoch.skillmatrix.skills.service.SkillsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static de.marckoch.skillmatrix.skills.web.ViewNames.DEVELOPER_LIST;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(DeveloperListController.class)
class DeveloperListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    DeveloperRepository developerRepository;

    @MockBean
    SkillRepository skillRepository;

    @MockBean
    SkillsService skillsService;

    @Test
    void showAllInitiallyRedirects() throws Exception {
        mockMvc.perform(get("/developers"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:developers/page/0?sort-field=lastName&sort-dir=asc"));
    }

    @Test
    void showAllShowsDeveloperList() throws Exception {
        Developer dev1 = new Developer();
        dev1.setLastName("developer1lastName");
        dev1.setExperiences(Collections.emptyList());

        when(developerRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(dev1)));

        mockMvc.perform(get("/developers/page/{pagenumber}", 0)
                        .param("sort-field", "lastName")
                        .param("sort-dir", "asc"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("developers"))
                .andExpect(model().attribute("developers", instanceOf(PageImpl.class)))
                .andExpect(model().attribute("currentPage", is(0)))
                .andExpect(model().attribute("totalPages", is(1)))
                .andExpect(model().attribute("totalItems", is(1L)))
                .andExpect(model().attribute("sortField", is("lastName")))
                .andExpect(model().attribute("sortDir", is("asc")))
                .andExpect(model().attribute("reverseSortDir", is("desc")))
                .andExpect(view().name(DEVELOPER_LIST));
    }
}