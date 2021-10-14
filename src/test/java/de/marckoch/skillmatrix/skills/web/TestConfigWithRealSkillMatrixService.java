package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import de.marckoch.skillmatrix.skills.service.SkillMatrixService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfigWithRealSkillMatrixService {

    @MockBean
    DeveloperRepository developerRepository;

    @MockBean
    SkillRepository skillRepository;

    @Bean
    SkillMatrixService create() {
        return new SkillMatrixService(skillRepository, developerRepository);
    }
}
