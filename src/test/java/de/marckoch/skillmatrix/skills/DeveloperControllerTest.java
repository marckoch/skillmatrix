package de.marckoch.skillmatrix.skills;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(DeveloperController.class)
class DeveloperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    DeveloperRepository developerRepository;

    @Test
    void testProcessFindFormById() throws Exception {
        Developer peter = new Developer();
        peter.setDeveloperId(123);
        peter.setFirstName("Peter");
        peter.setLastName("Parker");

        given(developerRepository.findById(peter.getDeveloperId())).willReturn(peter);

        mockMvc.perform(get("/developers/{developerId}", 123))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("developers/developerDetails"));
    }
}