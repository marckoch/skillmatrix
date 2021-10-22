package de.marckoch.skillmatrix.skills.web;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.CURRENT_PAGE;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.TOTAL_ITEMS;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.TOTAL_PAGES;

public class PaginationUtil {
    private PaginationUtil() {
    }

    public static void addPagingAttributesToModel(Model model, Page<?> resultPage, int pagenumber) {
        model.addAttribute(CURRENT_PAGE, pagenumber);
        model.addAttribute(TOTAL_PAGES, resultPage.getTotalPages());
        model.addAttribute(TOTAL_ITEMS, resultPage.getTotalElements());
    }
}