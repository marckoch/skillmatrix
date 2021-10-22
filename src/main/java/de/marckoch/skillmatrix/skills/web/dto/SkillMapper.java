package de.marckoch.skillmatrix.skills.web.dto;

import de.marckoch.skillmatrix.skills.entity.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper {

    public SkillDTO buildSkillDTO(Skill skill) {
        return SkillDTO.builder()
                .skillId(skill.getSkillId())
                .name(skill.getName())
                .version(skill.getVersion())
                .alias(skill.getAlias())
                .build();
    }

    public void updateEntityFromDTO(SkillDTO dto, Skill entity) {
        entity.setName(dto.getName());
        entity.setVersion(dto.getVersion());
        entity.setAlias(dto.getAlias());
    }
}
