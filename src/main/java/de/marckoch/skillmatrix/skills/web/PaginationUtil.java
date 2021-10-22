package de.marckoch.skillmatrix.skills.web;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

public class PaginationUtil {
    private PaginationUtil() {
    }

    public static void addPagingAttributesToModel(Model model, Page<?> resultPage, int pagenumber) {
        model.addAttribute("currentPage", pagenumber);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
    }
}
