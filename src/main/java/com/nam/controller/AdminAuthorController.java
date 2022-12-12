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
	
	private int pageSize=6;
	private String sortBy="id";
	
	@GetMapping("/management-author")
	public String managementAuthor(Model model, @ModelAttribute("message") String message,
			@ModelAttribute("error") String error) {
		System.out.println(message);
		Page<Author> authorPage = authorService.getPageAuthor(0, this.pageSize, this.sortBy);
		model.addAttribute("totalPages", authorPage.getTotalPages());
		model.addAttribute("totalElements", authorPage.getTotalElements());
		model.addAttribute("message", message.equals("") ? null : message);
		model.addAttribute("error", error.equals("") ? null : error);
		return "view/admin/management-author";
	}

	@GetMapping(value = "/new-author")
	public String showFormNewAuthor(Model model) {
		model.addAttribute("author", new Author());
		return "view/admin/form-add-new-author";
	}

	@PostMapping(value = "/save-author")
	public ModelAndView addnewAuthor(@ModelAttribute Author author, RedirectAttributes ra) {
		ModelAndView mav = new ModelAndView();
		try {
			Message message= authorService.save(author);
			ra.addFlashAttribute("message",message.getContent());
			mav.setViewName("redirect:/admin/management-author");
		} catch (Exception e) {
			mav = new ModelAndView("signin-up/common-error");
			mav.addObject("errorMsg", new ErrorMsgDto("Cannot save!"));
		}
		return mav;
	}

	@GetMapping(value = "/management-author-ajax", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String callAuthorAjax(@RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "5") int pageSize,
			@RequestParam(defaultValue = "id") String sortBy) {
		this.pageSize=pageSize;this.sortBy=sortBy;
		List<Author> authors = authorService.findAll(pageNo, pageSize, sortBy);
		int i=1;
		String html="";
		for(Author author: authors) {
			html += "<tr>"
					+ "                    <td>"+(pageNo*pageSize+i)+"</td>"
					+ "                    <td>"
					+ "                      <a href='#'"
					+ "                        ><img"
					+ "                          style='width: 20px'"
					+ "                          src='https://i.pinimg.com/236x/08/44/c5/0844c5eb33e92d674e6ad124bac4903a.jpg'"
					+ "                          class='avatar'"
					+ "                          alt='Avatar'"
					+ "                        />"
					+ "                        "+author.getFullname()+"</a"
					+ "                      >"
					+ "                    </td>"
					+ "                    <td>"+author.getDateOfBirth()+"</td>"
					+ "                    <td>"+author.getDescription()+"</td>"
					+ "                    <td>"
					+ "                      <a"
					+ "                        href='/admin/update-author/"+author.getId()+"'"
					+ "                        class='settings'"
					+ "                        title='Settings'"
					+ "                        data-toggle='tooltip'"
					+ "                        ><i class='material-icons'>&#xE8B8;</i></a"
					+ "                      >"
					+ "                      <a"
					+ "                        href='/admin/delete-author/"+author.getId()+"'"
					+ "                        class='delete'"
					+ "                        title='Delete'"
					+ "                        data-toggle='tooltip'"
					+ "                        ><i class='material-icons'>&#xE5C9;</i></a"
					+ "                      >"
					+ "                    </td>"
					+ "                  </tr>";
					i++;
			
		}
		
		return html;
	}
	
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
	
	@GetMapping("/delete-author/{id}")
	public ModelAndView deleteAuthor(@PathVariable("id") Long id,RedirectAttributes ra) {
		ModelAndView mav=new ModelAndView();
		try {
			Message message = authorService.delete(id);
			mav.setViewName("redirect:/admin/management-author");
			ra.addFlashAttribute("message", message.getContent());
		} catch (ObjectNotFoundException o) {
			mav.addObject("errorMsg", new ErrorMsgDto(o.getMessage()));
			mav.setViewName("redirect:/admin/management-author");
		}
		return mav;
	}
	

}
















