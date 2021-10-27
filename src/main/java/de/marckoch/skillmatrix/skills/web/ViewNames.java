package de.marckoch.skillmatrix.skills.web;

public class ViewNames {
    private ViewNames() {
        // no instance
    }

    public static final String DEVELOPER_DETAILS = "developers/developerDetails";
    public static final String DEVELOPER_LIST = "developers/developerList";
    public static final String CREATE_OR_UPDATE_DEVELOPER_VIEW = "developers/createOrUpdateDeveloperForm";
    public static final String CREATE_OR_UPDATE_PROJECT_VIEW = "developers/createOrUpdateProjectForm";

    public static final String SKILL_DETAILS = "skills/skillDetails";
    public static final String SKILL_LIST = "skills/skillList";
    public static final String CREATE_OR_UPDATE_SKILL_VIEW = "skills/createOrUpdateSkillForm";

    public static final String EXPERIENCE_EDIT_VIEW = "experiences/updateExperienceForm";

    public static final String SKILL_MATRIX = "skills/skillMatrix";
    public static final String SKILL_SETS = "skills/skillSets";

    public static final String SEARCH_RESULT = "globalsearch/searchResult";
    public static final String EMPTY_SEARCH = "globalsearch/emptySearch";

    public static final String REDIRECT_DEVELOPERS = "redirect:/developers";
    public static final String REDIRECT_SKILLS = "redirect:/skills";
}
