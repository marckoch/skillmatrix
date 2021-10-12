package de.marckoch.skillmatrix.skills;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class SkillsService {

    private final SkillRepository skillRepository;

    // find all Skills that this developer does NOT have
    public List<SelectItem> getFreeSkills(Developer dev) {
        List<Experience> experiences =  (dev.getExperiences() != null) ? dev.getExperiences() : Collections.emptyList();

        var skillsOfDeveloper = experiences
                .stream()
                .map(exp -> exp.getSkill().getSkillId())
                .toList();

        return skillRepository.findAll().stream()
                .filter(skill -> !skillsOfDeveloper.contains(skill.getSkillId()))
                .map(this::skill2SelectItem)
                .sorted(Comparator.comparing(SelectItem::getValue))
                .toList();
    }

    private SelectItem skill2SelectItem(Skill skill) {
        return new SelectItem(skill.getSkillId(), skill.getNameAndVersion());
    }
}
