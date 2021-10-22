package de.marckoch.skillmatrix.skills.web;

import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

public class SortUtil {
    private SortUtil() {
    }

    public static Sort build(String sortDir, String sortField) {
        return "desc".equalsIgnoreCase(sortDir) ?
                Sort.by(sortField).descending() :
                Sort.by(sortField).ascending();
    }

    private static String reverse(String sortDir) {
        return sortDir.equalsIgnoreCase("asc") ? "desc" : "asc";
    }

    public static void addSortAttributesToModel(Model model, String sortField, String sortDir) {
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverse(sortDir));
    }
}
