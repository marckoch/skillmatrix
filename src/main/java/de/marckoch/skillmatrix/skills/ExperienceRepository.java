package de.marckoch.skillmatrix.skills;

import org.springframework.data.repository.Repository;

public interface ExperienceRepository extends Repository<Experience, Integer> {

	void save(Experience experience);
}
