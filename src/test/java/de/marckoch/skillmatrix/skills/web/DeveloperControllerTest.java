package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import de.marckoch.skillmatrix.skills.service.SkillsService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(DeveloperController.class)
class DeveloperControllerTest {

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
                .andExpect(view().name("developers/developerList"));
    }

    @Test
    void showDeveloperShouldReturnDeveloperDetails() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);
        dev1.setLastName("developer1lastName");
        dev1.setExperiences(Collections.emptyList());

        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));

        mockMvc.perform(get("/developers/{developerId}", dev1.getDeveloperId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("developer", "experience"))
                .andExpect(view().name("developers/developerDetails"))
                .andExpect(content().string(containsString(dev1.getLastName())));
    }

    @Test
    void showDeveloperThrowsExceptionForUnknownDeveloper() throws Exception {
        when(developerRepository.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/developers/{developerId}", 123))
                .andExpect(status().isNotFound());
    }

    @Test
    void initCreationFormShouldShowNewDeveloper() throws Exception {
        mockMvc.perform(get("/developers/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("developerDTO"))
                .andExpect(model().attribute("developerDTO", hasProperty("new", is(true))))
                .andExpect(model().attribute("developerDTO", not(hasProperty("id"))))
                .andExpect(view().name("/developers/createOrUpdateDeveloperForm"));
    }

    @Test
    void processCreationFormWithWrongDataShouldShowError() throws Exception {
        // error because first and last name is missing in post!
        mockMvc.perform(post("/developers/new"))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(2))
                .andExpect(view().name("/developers/createOrUpdateDeveloperForm"));
    }

    @Test
    void processCreationFormWithCorrectDataShouldSaveData() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);

        when(developerRepository.save(ArgumentMatchers.any(Developer.class))).thenReturn(dev1);

        mockMvc.perform(post("/developers/new")
                        .param("firstName", "first")
                        .param("lastName", "last"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/developers/123"));
    }

    @Test
    void initUpdateDeveloperFormShouldShowExistingDeveloper() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);
        dev1.setFirstName("firstName123");
        dev1.setLastName("lastName123");

        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));

        mockMvc.perform(get("/developers/{developerId}/edit", 123))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("/developers/createOrUpdateDeveloperForm"))
                .andExpect(content().string(containsString("firstName123")))
                .andExpect(content().string(containsString("lastName123")));
    }

    @Test
    void processUpdateFormWithWrongDataShouldShowError() throws Exception {
        // error because first and last name is missing in post!
        mockMvc.perform(post("/developers/123/edit"))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(2))
                .andExpect(view().name("/developers/createOrUpdateDeveloperForm"));
    }

    @Test
    void processUpdateFormWithCorrectDataShouldSaveData() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);

        when(developerRepository.findById(123)).thenReturn(Optional.of(dev1));
        when(developerRepository.save(ArgumentMatchers.any(Developer.class))).thenReturn(dev1);

        mockMvc.perform(post("/developers/123/edit")
                        .param("firstName", "first")
                        .param("lastName", "last"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/developers/123"));
    }
}