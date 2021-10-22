package de.marckoch.skillmatrix.skills.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Integer> {

	@Query("SELECT s FROM Skill s WHERE s.skillId =:id")
	@Transactional(readOnly = true)
	Optional<Skill> findById(@Param("id") Integer id);

	@Query("SELECT DISTINCT s FROM Skill s WHERE UPPER(s.name) LIKE %:queryInUpperCase% OR UPPER(s.alias) LIKE %:queryInUpperCase%")
	@Transactional(readOnly = true)
	List<Skill> findByQuery(@Param("queryInUpperCase") String queryInUpperCase);

	@Query("SELECT DISTINCT s FROM Skill s LEFT JOIN FETCH s.experiences WHERE UPPER(s.name) LIKE %:queryInUpperCase% OR UPPER(s.alias) LIKE %:queryInUpperCase%")
	@Transactional(readOnly = true)
	List<Skill> findWithExperiencesByQuery(@Param("queryInUpperCase") String queryInUpperCase);

	@Query("SELECT DISTINCT s, e, d FROM Skill s LEFT JOIN FETCH s.experiences e LEFT JOIN FETCH e.developer d")
	@Transactional(readOnly = true)
	List<Skill> findAllForSkillMatrix();

	@Query("SELECT DISTINCT s FROM Skill s")
	@Transactional(readOnly = true)
	List<Skill> findAllForFreeSkills();

	@Query(value = "SELECT s FROM Skill s LEFT JOIN FETCH s.experiences e LEFT JOIN FETCH e.developer d",
			countQuery = "SELECT COUNT(s) FROM Skill s")
	@Transactional(readOnly = true)
	Page<Skill> findAllInSkillList(Pageable pageable);

	Skill save(Skill skill);
}
