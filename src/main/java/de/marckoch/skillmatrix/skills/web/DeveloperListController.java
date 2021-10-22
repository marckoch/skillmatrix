package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.DEVELOPERS;
import static de.marckoch.skillmatrix.skills.web.RequestParams.SORT_DIR;
import static de.marckoch.skillmatrix.skills.web.RequestParams.SORT_FIELD;
import static de.marckoch.skillmatrix.skills.web.SortDirection.ASC;
import static de.marckoch.skillmatrix.skills.web.SortField.DEVELOPER_LAST_NAME;
import static de.marckoch.skillmatrix.skills.web.ViewNames.DEVELOPER_LIST;

@Controller
@AllArgsConstructor
class DeveloperListController {

	private final DeveloperRepository developerRepository;

	@GetMapping("/developers")
	public String showAll() {
		return "redirect:developers/page/0?sort-field=lastName&sort-dir=asc";
	}

	@GetMapping("/developers/page/{pagenumber}")
	public String showAll(@PathVariable int pagenumber,
						  @RequestParam(name = SORT_FIELD, defaultValue = DEVELOPER_LAST_NAME) final String sortField,
						  @RequestParam(name = SORT_DIR, defaultValue = ASC) final String sortDir,
						  Model model) {
		Sort sort = SortUtil.build(sortDir, sortField);
		Pageable p = PageRequest.of(pagenumber, 10, sort);
		Page<Developer> resultPage = developerRepository.findAllInDeveloperList(p);

		model.addAttribute(DEVELOPERS, resultPage);

		PaginationUtil.addPagingAttributesToModel(model, resultPage, pagenumber);
		SortUtil.addSortAttributesToModel(model, sortField, sortDir);

		return DEVELOPER_LIST;
	}
}
