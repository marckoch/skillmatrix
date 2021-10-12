package de.marckoch.skillmatrix.skills;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(DeveloperController.class)
class DeveloperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    DeveloperRepository developerRepository;

    @MockBean
    SkillRepository skillRepository;

    @Test
    void findAllShouldReturnDeveloperList() throws Exception {
        Developer dev1 = new Developer();
        dev1.setLastName("developer1lastName");
        dev1.setExperiences(Collections.emptyList());

        when(developerRepository.findAll()).thenReturn(List.of(dev1));

        mockMvc.perform(get("/developers"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("developers"))
                .andExpect(view().name("developers/developerList"))
                .andExpect(content().string(containsString(dev1.getLastName())));
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
    void initCreationFormShouldShowNewDeveloper() throws Exception {
        mockMvc.perform(get("/developers/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("developer"))
                .andExpect(model().attribute("developer", hasProperty("new", is(true))))
                .andExpect(model().attribute("developer", not(hasProperty("id"))))
                .andExpect(view().name("/developers/createOrUpdateDeveloperForm"));
    }

    @Test
    void processCreationFormWithWrongDataShouldShowError() throws Exception {
        // error because first and last name is missing in post!
        mockMvc.perform(post("/developers/new"))
                .andDo(print())
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
                .andDo(print())
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
                .andDo(print())
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
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(2))
                .andExpect(view().name("/developers/createOrUpdateDeveloperForm"));
    }

    @Test
    void processUpdateFormWithCorrectDataShouldSaveData() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);

        when(developerRepository.save(ArgumentMatchers.any(Developer.class))).thenReturn(dev1);

        mockMvc.perform(post("/developers/123/edit")
                        .param("firstName", "first")
                        .param("lastName", "last"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/developers/123"));
    }

    @Test
    void testProcessFindFormById() throws Exception {
        Developer peter = new Developer();
        peter.setDeveloperId(123);
        peter.setFirstName("Peter");
        peter.setLastName("Parker");

        Skill java = new Skill();
        java.setName("Java");

        Experience e1 = new Experience();
        e1.setDeveloper(peter);
        e1.setSkill(java);
        e1.setYears(12);
        e1.setRating(5);
        peter.setExperiences(List.of(e1));

        given(developerRepository.findById(peter.getDeveloperId())).willReturn(Optional.of(peter));

        mockMvc.perform(get("/developers/{developerId}", 123))
                .andExpect(status().isOk())
                .andExpect(view().name("developers/developerDetails"));
    }
}