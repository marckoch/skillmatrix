package de.marckoch.skillmatrix.skills.service;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.entity.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class SkillsService {

    private final SkillRepository skillRepository;

    /**
     * find all Skills that this developer does NOT have
     */
    public List<Skill> getFreeSkills(Developer dev) {
        List<Experience> experiences =  (dev.getExperiences() != null) ? dev.getExperiences() : Collections.emptyList();

        var skillsOfDeveloper = experiences.stream()
                .map(exp -> exp.getSkill().getSkillId())
                .toList();

        return skillRepository.findAll().stream()
                .filter(skill -> !skillsOfDeveloper.contains(skill.getSkillId()))
                .toList();
    }
}
