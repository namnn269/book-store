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
import com.nam.entity.Author;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.service.IAuthorService;

@Controller
@RequestMapping("/admin")
public class AdminAuthorController {
	@Autowired
	private IAuthorService authorService;
	
	/* Hiển thị trang admin thông tin của các tác giả */
	@GetMapping("/management-author")
	public String managementAuthor(Model model, @ModelAttribute("message") String message,
			@ModelAttribute("error") String error) {
		model.addAttribute("message", message.equals("") ? null : message);
		model.addAttribute("error", error.equals("") ? null : error);
		return "view/admin/management-author";
	}

	/* Hiển thị form để thêm tác giả */
	@GetMapping(value = "/new-author")
	public String showFormNewAuthor(Model model) {
		model.addAttribute("author", new Author());
		return "view/admin/form-add-new-author";
	}

	/* Gọi service để thêm hoặc cập nhật tác giả */
	@PostMapping(value = "/save-author")
	public ModelAndView addnewAuthor(@ModelAttribute Author author, RedirectAttributes ra) {
		ModelAndView mav = new ModelAndView();
		try {
			Message message= authorService.save(author);
			ra.addFlashAttribute("message",message.getContent());
			mav.setViewName("redirect:/admin/management-author");
		} catch (Exception e) {
			mav = new ModelAndView("view/error/common-error");
			mav.addObject("errorMsg", new ErrorMsgDto("Cannot save!"));
		}
		return mav;
	}

	/* Lấy tác giả theo ID để binding vào form update tác giả */
	@GetMapping("/update-author/{id}")
	public ModelAndView showFormUpdate(@PathVariable("id") Long id) {
		ModelAndView mav=new ModelAndView();
		Optional<Author> author=authorService.findById(id);
		try {
			mav.addObject("author", author.get());
			mav.setViewName("view/admin/form-add-new-author");
		} catch (Exception e) {
			mav.setViewName("redirect:/admin/management-author");
		}
		return mav;
	}
	
	/* Lấy ID của tác giả, gọi service để xóa */
	@GetMapping(value = "/delete-author", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String deleteAuthor(@RequestParam("id") Long id,RedirectAttributes ra) {
		Message message;
		try {
			 message = authorService.delete(id);
			ra.addFlashAttribute("message", message.getContent());
		} catch (ObjectNotFoundException o) {
			 message =new Message(o.getMessage());
		}
		return message.getContent();
	}
	
	/* Trả về 1 mảng HTML
	 * Phần tử 1 chứa HTML thông tin tác giả
	 * Phẩn tử 2 chứa HTML chứa thông tin pagination */
	@GetMapping(value = "/management-author-ajax")
	@ResponseBody
	public String[] callAuthorAjax(	@RequestParam(defaultValue = "0") int pageNo,
									@RequestParam(defaultValue = "6") int pageSize,
									@RequestParam(defaultValue = "id") String sortBy,
									@RequestParam(defaultValue = "") String searchFor) {
		List<Author> authors = authorService.findAll(pageNo, pageSize, sortBy, searchFor);
		int i = 1;
		String html="";
		for(Author author: authors) {
			html += "<tr>"
					+ "                    <td>" + (pageNo * pageSize + i) + " &nbsp; "
					+ "						  <input class='delete-many-input' type='checkbox' value='"+author.getId()+"' />"
					+ "					   </td>"
					+ "                    <td style='font-weight:bold;'>"+ author.getFullname() +"</td>"
					+ "                    <td>"+author.getDescription()+"</td>"
					+ "                    <td>"
					+ "                      <a"
					+ "                        href='/admin/update-author/"+author.getId()+"'"
					+ "                        class='settings'"
					+ "                        title='Settings'"
					+ "                        data-toggle='tooltip'"
					+ "                        ><i class='material-icons'>&#xE8B8;</i></a"
					+ "                      >"
					+ "                      <button"
					+ "                        value="+author.getId()
					+ "                        class='delete'"
					+ "                        title='Delete'"
					+ "                        data-toggle='tooltip'"
					+ "                        ><i class='material-icons'>&#xE5C9;</i></button>"
					+ "                    </td>"
					+ "                  </tr>";
					i++;
			
		}
		String paginationHtml=getPaginationString(pageNo, pageSize, sortBy, searchFor);
		return new String[] {html, paginationHtml};
	}
	
	/* Trả về HTML chứa thông tin pagination tác giả */
	private  String getPaginationString(int pageNo, int pageSize, String sortBy, String searchFor) {
		Page<Author> authorPage = authorService.getPageAuthor(pageNo, pageSize, sortBy, searchFor);
		String html =			"<div class='hint-text'>"
				+ "                Tổng"
				+ "                <b>"+authorPage.getTotalElements()+"</b> tác giả"
				+ "              </div>"
				+ "              <ul class='pagination'>"
				+ "                <li class='previous-page-item'>"
				+ "                  <a href='#' class='page-link'>Previous</a>"
				+ "                </li>";
				
		for (int i = 1; i <= authorPage.getTotalPages(); i++) {
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
















