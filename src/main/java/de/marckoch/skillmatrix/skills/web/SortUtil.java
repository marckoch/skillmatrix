package de.marckoch.skillmatrix.skills.web;

import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.REVERSE_SORT_DIR;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SORT_DIR;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.SORT_FIELD;
import static de.marckoch.skillmatrix.skills.web.SortDirection.ASC;
import static de.marckoch.skillmatrix.skills.web.SortDirection.DESC;

public class SortUtil {
    private SortUtil() {
    }

    public static Sort build(String sortDir, String sortField) {
        return DESC.equalsIgnoreCase(sortDir) ?
                Sort.by(sortField).descending() :
                Sort.by(sortField).ascending();
    }

    private static String reverse(String sortDir) {
        return sortDir.equalsIgnoreCase(ASC) ? DESC : ASC;
    }

    public static void addSortAttributesToModel(Model model, String sortField, String sortDir) {
        model.addAttribute(SORT_FIELD, sortField);
        model.addAttribute(SORT_DIR, sortDir);
        model.addAttribute(REVERSE_SORT_DIR, reverse(sortDir));
    }
}