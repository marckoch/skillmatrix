package de.marckoch.skillmatrix.globalsearch.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.Skill;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SearchResultHighlighter {

    private static final String HIGHLIGHTED_RESULT = "<mark>$0</mark>";

    public void highlightDeveloperSearchMatches(Collection<Developer> developers, String query) {
        developers.forEach(d -> {
            if (d.getFirstName() != null) {
                d.setFirstName(d.getFirstName().replaceAll(caseIgnore(query), HIGHLIGHTED_RESULT));
            }
            if (d.getLastName() != null) {
                d.setLastName(d.getLastName().replaceAll(caseIgnore(query), HIGHLIGHTED_RESULT));
            }
        });
    }

    public void highlightSkillSearchMatches(Collection<Skill> skills, String query) {
        skills.forEach(s -> {
            if (s.getName() != null) {
                s.setName(s.getName().replaceAll(caseIgnore(query), HIGHLIGHTED_RESULT));
            }
            if (s.getAlias() != null) {
                s.setAlias(s.getAlias().replaceAll(caseIgnore(query), HIGHLIGHTED_RESULT));
            }
        });
    }

    private String caseIgnore(String query) {
        return "(?i)" + query;
    }
}
