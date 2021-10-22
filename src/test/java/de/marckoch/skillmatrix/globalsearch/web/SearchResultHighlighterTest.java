package de.marckoch.skillmatrix.globalsearch.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.Skill;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SearchResultHighlighterTest {

    @Test
    void highlightingDevelopersWorks() {
        // given
        Developer dev1 = Developer.builder()
                .firstName("firsttestName")
                .lastName("lastTestName").build();
        Developer dev2 = Developer.builder()
                .lastName("lastTESTName").build();
        Developer dev3 = Developer.builder()
                .firstName("firsttestName").build();

        List<Developer> devs = List.of(dev1, dev2, dev3);

        // when
        new SearchResultHighlighter().highlightDeveloperSearchMatches(devs, "test");

        // then
        assertThat(devs.get(0).getFirstName()).isEqualTo("first<mark>test</mark>Name");
        assertThat(devs.get(0).getLastName()).isEqualTo("last<mark>Test</mark>Name");
        assertThat(devs.get(1).getLastName()).isEqualTo("last<mark>TEST</mark>Name");
        assertThat(devs.get(2).getFirstName()).isEqualTo("first<mark>test</mark>Name");
    }

    @Test
    void highlightingSkillsWorks() {
        // given
        Skill s1 = Skill.builder()
                .name("skilltestName")
                .alias("aliasTestAlias").build();
        Skill s2 = Skill.builder()
                .name("skillTESTName").build();
        Skill s3 = Skill.builder()
                .alias("ALIAStestALIAS").build();

        List<Skill> skills = List.of(s1,s2,s3);

        // when
        new SearchResultHighlighter().highlightSkillSearchMatches(skills, "test");

        // then
        assertThat(skills.get(0).getName()).isEqualTo("skill<mark>test</mark>Name");
        assertThat(skills.get(0).getAlias()).isEqualTo("alias<mark>Test</mark>Alias");
        assertThat(skills.get(1).getName()).isEqualTo("skill<mark>TEST</mark>Name");
        assertThat(skills.get(2).getAlias()).isEqualTo("ALIAS<mark>test</mark>ALIAS");
    }
}