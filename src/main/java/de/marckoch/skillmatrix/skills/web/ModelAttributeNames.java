package de.marckoch.skillmatrix.skills.web;

public enum ModelAttributeNames {
    DEVELOPER("developer"),
    DEVELOPER_DTO("developerDTO"),
    EXPERIENCE("experience"),
    PROJECT_DTO("projectDTO"),
    SKILL("skill"),
    SKILL_DTO("skillDTO")
    ;

    final String modelAttributeName;

    ModelAttributeNames(String modelAttributeName) {
        this.modelAttributeName = modelAttributeName;
    }

    public String modelAttributeName() {
        return this.modelAttributeName;
    }
}
