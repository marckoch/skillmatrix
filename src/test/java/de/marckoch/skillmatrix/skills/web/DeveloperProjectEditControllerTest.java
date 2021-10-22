package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Project;
import de.marckoch.skillmatrix.skills.entity.ProjectRepository;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import de.marckoch.skillmatrix.skills.service.SkillsService;
import de.marckoch.skillmatrix.skills.web.dto.ProjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.YearMonth;
import java.util.Optional;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.PROJECT_DTO;
import static de.marckoch.skillmatrix.skills.web.ViewNames.CREATE_OR_UPDATE_PROJECT_VIEW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest({DeveloperProjectEditController.class, ProjectMapper.class})
class DeveloperProjectEditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    DeveloperRepository developerRepository;

    @MockBean
    SkillRepository skillRepository;

    @MockBean
    ProjectRepository projectRepository;

    @MockBean
    SkillsService skillsService;

    @Test
    void initCreationFormShouldShowNewProject() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);
        dev1.setFirstName("firstName");
        dev1.setLastName("lastName");

        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));

        mockMvc.perform(get("/developers/123/project/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(PROJECT_DTO))
                .andExpect(model().attribute(PROJECT_DTO, hasProperty("new", is(true))))
                .andExpect(model().attribute(PROJECT_DTO, not(hasProperty("id"))))
                .andExpect(view().name(CREATE_OR_UPDATE_PROJECT_VIEW));
    }

    @Test
    void processCreationFormWithWrongDataShouldShowError() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);
        dev1.setFirstName("firstName");
        dev1.setLastName("lastName");

        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));

        // error because name, since and until are missing in post!
        mockMvc.perform(post("/developers/123/project/add"))
                .andExpect(status().isOk())
                .andExpect(model().errorCount(3))
                .andExpect(model().attributeHasFieldErrorCode(PROJECT_DTO, "name", "NotEmpty"))
                .andExpect(model().attributeHasFieldErrorCode(PROJECT_DTO, "since", "NotEmpty"))
                .andExpect(model().attributeHasFieldErrorCode(PROJECT_DTO, "until", "NotEmpty"))
                .andExpect(view().name(CREATE_OR_UPDATE_PROJECT_VIEW));
    }

    @Test
    void processCreationFormWithCorrectDataShouldSaveData() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);

        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));
        when(developerRepository.save(ArgumentMatchers.any(Developer.class))).thenReturn(dev1);

        mockMvc.perform(post("/developers/{developerId}/project/add", dev1.getDeveloperId())
                        .param("name", "my test project")
                        .param("since", "2019-12")
                        .param("until", "2022-03"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/developers/123"));
    }

    @Test
    void initUpdateDeveloperFormShouldShowExistingProject() throws Exception {
        Project p = Project.builder()
                .name("my project")
                .since(YearMonth.of(2019, 3))
                .until(YearMonth.of(2021, 11))
                .build();
        Developer dev1 = Developer.builder()
                .developerId(123)
                .currentProject(p)
                .build();

        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));

        mockMvc.perform(get("/developers/{developerId}/project/edit", dev1.getDeveloperId()))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(CREATE_OR_UPDATE_PROJECT_VIEW))
                .andExpect(content().string(containsString("my project")));
    }

    @Test
    void processUpdateFormWithWrongDataShouldShowError() throws Exception {
        Developer dev1 = new Developer();
        dev1.setDeveloperId(123);
        dev1.setFirstName("firstName");
        dev1.setLastName("lastName");

        when(developerRepository.findById(dev1.getDeveloperId())).thenReturn(Optional.of(dev1));

        // error because name is missing in post!
        mockMvc.perform(post("/developers/123/project/edit"))
                .andExpect(status().isOk())
                .andExpect(model().errorCount(3))
                .andExpect(model().attributeHasFieldErrorCode(PROJECT_DTO, "name", "NotEmpty"))
                .andExpect(model().attributeHasFieldErrorCode(PROJECT_DTO, "since", "NotEmpty"))
                .andExpect(model().attributeHasFieldErrorCode(PROJECT_DTO, "until", "NotEmpty"))
                .andExpect(view().name(CREATE_OR_UPDATE_PROJECT_VIEW));
    }

    @Test
    void processUpdateFormWithCorrectDataShouldSaveData() throws Exception {
        Project p = Project.builder()
                .name("my project")
                .since(YearMonth.of(2019, 3))
                .until(YearMonth.of(2021, 11))
                .build();
        Developer dev1 = Developer.builder()
                .developerId(123)
                .currentProject(p)
                .build();

        when(developerRepository.findById(123)).thenReturn(Optional.of(dev1));
        when(developerRepository.save(ArgumentMatchers.any(Developer.class))).thenReturn(dev1);

        mockMvc.perform(post("/developers/{developerId}/project/edit", dev1.getDeveloperId())
                        .param("name", "my test project")
                        .param("since", "2019-12")
                        .param("until", "2022-03"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/developers/123"));
    }

    @Test
    void deleteProjectWorks() throws Exception {
        Project p = Project.builder()
                .name("my project")
                .since(YearMonth.of(2019, 3))
                .until(YearMonth.of(2021, 11))
                .build();
        Developer dev1 = Developer.builder()
                .developerId(123)
                .currentProject(p)
                .build();

        when(developerRepository.findById(123)).thenReturn(Optional.of(dev1));
        when(developerRepository.save(dev1)).thenReturn(dev1);

        mockMvc.perform(get("/developers/{developerId}/project/delete", dev1.getDeveloperId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/developers/123"));

        // project has been set null
        assertThat(dev1.hasProject()).isFalse();
    }
}