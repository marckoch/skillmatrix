package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.service.SkillMatrixService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static de.marckoch.skillmatrix.skills.web.ViewNames.SKILL_MATRIX;

@Controller
@AllArgsConstructor
class SkillMatrixController {

    private final SkillMatrixService skillMatrixService;

    @GetMapping("/skills/matrix")
    public String skillMatrix(Model model) {
        final List<Skill> skills = skillMatrixService.buildSkillMatrix();
        model.addAttribute("skills", skills);
        return SKILL_MATRIX;
    }
}
