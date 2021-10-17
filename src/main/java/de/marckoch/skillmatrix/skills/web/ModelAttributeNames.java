package de.marckoch.skillmatrix.skills.web;

public enum ModelAttributeNames {
    DEVELOPER("developer"),
    DEVELOPER_DTO("developerDTO"),
    EXPERIENCE_DTO("experienceDTO"),
    PROJECT_DTO("projectDTO"),
    SKILL("skill"),
    SKILL_DTO("skillDTO")
    ;

    final String modelAttributeName;

    ModelAttributeNames(String modelAttributeName) {
        this.modelAttributeName = modelAttributeName;
    }
}
