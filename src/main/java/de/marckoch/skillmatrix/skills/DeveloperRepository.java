package de.marckoch.skillmatrix.skills;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface DeveloperRepository extends Repository<Developer, Integer> {

	@Query("SELECT dev FROM Developer dev left join fetch dev.experiences WHERE dev.developerId =:id")
	@Transactional(readOnly = true)
	Developer findById(@Param("id") Integer id);

	@Query("SELECT DISTINCT dev FROM Developer dev left join fetch dev.experiences WHERE dev.lastName LIKE :lastName%")
	@Transactional(readOnly = true)
	Collection<Developer> findByLastName(@Param("lastName") String lastName);

	@Query("SELECT DISTINCT dev FROM Developer dev join fetch dev.experiences exp join fetch exp.skill")
	@Transactional(readOnly = true)
	Collection<Developer> findAll();
}
