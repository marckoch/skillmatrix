package de.marckoch.skillmatrix.skills.web;

import org.springframework.data.domain.Page;
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

    static String reverse(String sortDir) {
        return sortDir.equalsIgnoreCase("asc") ? "desc" : "asc";
    }

    public static void addPagingAndSortAttributesToModel(Model model, Page<?> resultPage, int pagenumber, String sortField, String sortDir) {
        // paging
        model.addAttribute("currentPage", pagenumber);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());

        // sorting
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverse(sortDir));
    }
}
