package de.marckoch.skillmatrix.skills.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DeveloperRepository extends JpaRepository<Developer, Integer> {

	@Query("SELECT dev FROM Developer dev WHERE dev.developerId =:id")
	@Transactional(readOnly = true)
	Optional<Developer> findById(@Param("id") Integer id);

	@Query("SELECT DISTINCT dev, p FROM Developer dev LEFT JOIN dev.currentProject p WHERE UPPER(dev.lastName) LIKE %:queryInUpperCase% OR UPPER(dev.firstName) LIKE %:queryInUpperCase%")
	@Transactional(readOnly = true)
	Collection<Developer> findByQuery(@Param("queryInUpperCase") String queryInUpperCase);

	@Query("SELECT DISTINCT dev, e FROM Developer dev LEFT JOIN FETCH dev.experiences e")
	@Transactional(readOnly = true)
	List<Developer> findAllForSkillMatrix();

	@Query(value = "SELECT dev FROM Developer dev LEFT JOIN FETCH dev.currentProject p LEFT JOIN FETCH dev.experiences e LEFT JOIN FETCH e.skill s",
	countQuery = "SELECT COUNT(dev) FROM Developer dev")
	@Transactional(readOnly = true)
	Page<Developer> findAllInDeveloperList(Pageable pageable);

	Developer save(Developer developer);
}
