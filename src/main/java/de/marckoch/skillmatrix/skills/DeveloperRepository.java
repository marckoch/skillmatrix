package de.marckoch.skillmatrix.skills;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface DeveloperRepository extends Repository<Developer, Integer> {

	@Query("SELECT dev FROM Developer dev WHERE dev.developerId =:id")
	@Transactional(readOnly = true)
	Developer findById(@Param("id") Integer id);

	@Query("SELECT DISTINCT dev FROM Developer dev WHERE UPPER(dev.lastName) LIKE %:queryInUpperCase% OR UPPER(dev.firstName) LIKE %:queryInUpperCase%")
	@Transactional(readOnly = true)
	Collection<Developer> findByQuery(@Param("queryInUpperCase") String queryInUpperCase);

	@Query("SELECT DISTINCT dev FROM Developer dev")
	@Transactional(readOnly = true)
	Collection<Developer> findAll();

	void save(Developer developer);
}
