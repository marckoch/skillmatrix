package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import de.marckoch.skillmatrix.skills.service.SkillSetsService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfigWithRealSkillSetsService {

    @MockBean
    DeveloperRepository developerRepository;

    @MockBean
    SkillRepository skillRepository;

    @Bean
    SkillSetsService create() {
        return new SkillSetsService(skillRepository, developerRepository);
    }
}
