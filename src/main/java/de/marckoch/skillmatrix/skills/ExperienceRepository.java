package de.marckoch.skillmatrix.skills;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience, Integer> {

	@Query("SELECT e FROM Experience e WHERE e.experienceid =:id")
	@Transactional(readOnly = true)
	Optional<Experience> findById(@Param("id") Integer id);

	Experience save(Experience experience);

	void deleteById(Integer experienceId);
}
