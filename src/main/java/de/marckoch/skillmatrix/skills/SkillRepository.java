package de.marckoch.skillmatrix.skills;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface SkillRepository extends Repository<Skill, Integer> {

	@Query("SELECT s FROM Skill s WHERE s.skillId =:id")
	@Transactional(readOnly = true)
	Skill findById(@Param("id") Integer id);

	@Query("SELECT DISTINCT s FROM Skill s WHERE UPPER(s.name) LIKE %:queryInUpperCase%")
	@Transactional(readOnly = true)
	Collection<Skill> findByQuery(@Param("queryInUpperCase") String queryInUpperCase);

	@Query("SELECT DISTINCT s FROM Skill s")
	@Transactional(readOnly = true)
	Collection<Skill> findAll();

	void save(Skill skill);
}
