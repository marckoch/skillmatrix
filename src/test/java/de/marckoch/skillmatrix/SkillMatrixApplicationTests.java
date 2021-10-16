package de.marckoch.skillmatrix;

import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SkillMatrixApplicationTests {

	@Autowired
	DeveloperRepository developerRepository;

	@Test
	void contextLoads() {
		Assertions.assertThat(developerRepository).isNotNull();
	}
}
