package com.nam.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nam.dto.ErrorMsgDto;
import com.nam.entity.Category;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectAlreadyExistedException;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.service.ICategoryService;

@Controller
@RequestMapping("/admin")
public class AdminCategoryController {

	@Autowired
	private ICategoryService categoryService;

	@GetMapping("/management-category")
	public String categoryManagement(Model model, @ModelAttribute("message") String message,
									@ModelAttribute("error") String error) {
		model.addAttribute("message", message.equals("")?null:message);
		model.addAttribute("error", error.equals("")?null:error);
		return "view/admin/management-category";
	}

	// creating form 
	@GetMapping(value = "/new-category")
	public String showCategoryForm(Model model) {
			model.addAttribute("category", new Category());
		return "view/admin/form-add-new-category";
	}
	
	// editing form
	@GetMapping("/update-category/{id}")
	public ModelAndView showEditCategory(@PathVariable("id") Long id) {
		Optional<Category> category=categoryService.findById(id);
		ModelAndView mav=new ModelAndView("view/admin/form-add-new-category");
		if(category.isPresent()) {
				mav.addObject("category",category.get());
		}else {
			mav.addObject("errorMsg",  new ErrorMsgDto("Không hợp lệ"));
			mav.setViewName("view/admin/form-add-new-category");
		}
		return mav;
	}
	
	// handle create and editing form
	@PostMapping(value = "/save-category")
	public ModelAndView addNewCategory(@ModelAttribute Category category, RedirectAttributes ra) {
		ModelAndView mav = new ModelAndView();
		try {
			Message message = categoryService.save(category);
			ra.addFlashAttribute("message", message.getContent());
			mav.setViewName("redirect:/admin/management-category");
		} catch (ObjectAlreadyExistedException o) {
			ErrorMsgDto errorMsgDto = new ErrorMsgDto(o.getMessage());
			mav.addObject("errorMsg", errorMsgDto);
//			mav.addObject("category",category);
			mav.setViewName("view/admin/form-add-new-category");
		} catch (Exception e) {
			mav.setViewName("view/error/error-page");
		}
		return mav;
	}
	
	// handle delete category
	@GetMapping(value = "/delete-category", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String deleteCategory(@RequestParam("id") Long id) {
		Message message;
		try {
			 message = categoryService.delete(id);
		} catch (ObjectNotFoundException o) {
			message=new Message(o.getMessage());
		}
		return message.getContent();
	}
	
	// providing list category by ajax
	@GetMapping(value = "/management-category-ajax")
	@ResponseBody
	public String[] callCategoryAjax(	@RequestParam(defaultValue = "0") int pageNo,
										@RequestParam(defaultValue = "6") int pageSize,
										@RequestParam(defaultValue = "id") String sortBy,
										@RequestParam(defaultValue = "") String searchFor) {
		String html = "";
		List<Category> list = categoryService.findAll(pageNo, pageSize, sortBy, searchFor);
		int i = 1;
		for (Category category : list) {
			html += " <tr>"
					+ "                    <td>" + (pageNo * pageSize + i) + "</td>"
					+ "                    <td>"
					+ "                      <a href='#'"
					+ "                        ><img"
					+ "                          style='width: 20px'"
					+ "                          src='https://i.pinimg.com/236x/08/44/c5/0844c5eb33e92d674e6ad124bac4903a.jpg'"
					+ "                          class='avatar'"
					+ "                          alt='Avatar'"
					+ "                        />"
					+ "                        " + category.getCategoryTitle() + "</a"
					+ "                      >"
					+ "                    </td>"
					+ "                    <td>"
					+ "                      <a"
					+ "                        href='/admin/update-category/" + category.getId() + "'"
					+ "                        class='settings'"
					+ "                        title='Settings'"
					+ "                        data-toggle='tooltip'"
					+ "                        ><i class='material-icons'>&#xE8B8;</i></a"
					+ "                      >"
					+ "                      <button"
					+ "                        value=" + category.getId()
					+ "                        class='delete'"
					+ "                        title='Delete'"
					+ "                        data-toggle='tooltip'"
					+ "                        ><i class='material-icons'>&#xE5C9;</i></button>"
					+ "                    </td>"
					+ "                  </tr>";
			i++;
		}
		
		String htmlPagination = getPaginationString(pageNo, pageSize, sortBy, searchFor);
		
		return new String[] { html, htmlPagination};
	}

	private  String getPaginationString(int pageNo, int pageSize, String sortBy, String searchFor) {
		Page<Category> categoryPage = categoryService.getPageCategory(pageNo, pageSize, sortBy, searchFor);
		String html =			"<div class='hint-text'>"
				+ "                Tổng"
				+ "                <b>"+categoryPage.getTotalElements()+"</b> thể loại"
				+ "              </div>"
				+ "              <ul class='pagination'>"
				+ "                <li class='previous-page-item'>"
				+ "                  <a href='#' class='page-link'>Previous</a>"
				+ "                </li>";
				
		for (int i = 1; i <= categoryPage.getTotalPages(); i++) {
			html += "                  <li class='page-item " + (i == pageNo + 1 ? "active":" ")+"'>"
				+ "                    <a href='#' class='page-link'>"+i+"</a>"
				+ "                  </li>";
		}
				
				html+=
				 "                <li class='next-page-item'>"
				+ "                  <a href='#' class='page-link'>Next</a>"
				+ "                </li>"
				+ "              </ul>";
		
		return html;
	}
}
















