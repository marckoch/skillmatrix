package de.marckoch.skillmatrix.skills;

import org.junit.jupiter.api.Test;

class SkillsTest {

	@Test
	void create() {
		Developer developer = new Developer();
		developer.setFirstName("Marc");
		developer.setLastName("Koch");
		developer.setTitle("Dipl-Phys");

		Skill skill = new Skill();
		skill.setName("Java");

		Experience experience = new Experience();
		experience.setDeveloper(developer);
		experience.setSkill(skill);

	}

}
