package de.marckoch.skillmatrix.skills;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class SkillsRestController {

    private final SkillRepository skillRepository;

    @GetMapping(value = "/skills/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String, String>> skillsAsJson(@RequestParam String q) {
        List<Skill> allSkills = skillRepository.findByQuery(q.toUpperCase());
        allSkills.forEach(skill -> skill.setExperiences(new ArrayList<>()));
        return allSkills.stream().map(this::toMap).toList();
    }

    private Map<String, String> toMap(Skill skill) {
        Map<String, String> m = new HashMap<>();
        m.put("id", skill.getSkillId().toString());
        m.put("name", skill.getName());
        return m;
    }
}
