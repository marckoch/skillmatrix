package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Skill;
import de.marckoch.skillmatrix.skills.service.SkillMatrixService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
class SkillMatrixController {

    private final SkillMatrixService skillMatrixService;

    @GetMapping("/skills/matrix")
    public String skillMatrix(Model model) {
        final List<Skill> skills = skillMatrixService.buildSkillMatrix();
        model.addAttribute("skills", skills);
        return "/skills/skillMatrix";
    }
}
