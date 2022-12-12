package com.nam.controller;

import java.util.List;

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

import com.nam.dto.DisplayBookDto;
import com.nam.dto.ErrorMsgDto;
import com.nam.dto.NewBookDto;
import com.nam.entity.Author;
import com.nam.entity.Book;
import com.nam.entity.Category;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.service.IAuthorService;
import com.nam.service.IBookService;
import com.nam.service.ICategoryService;

@Controller
@RequestMapping("/admin")
public class AdminBookController {

	@Autowired
	private IBookService bookService;
	
	@Autowired
	private ICategoryService categoryService;
	
	@Autowired 
	IAuthorService authorService;
	
	private int pageNo=0;
	private int pageSize=6;
	private String sortBy="id";
	@GetMapping(value = "/management-book")
	public String bookManagement(Model model, @ModelAttribute("message") String message,
								@ModelAttribute("error") String error) {
		Page<Book> bookPage = bookService.getPageBook(pageNo, pageSize, sortBy);

		model.addAttribute("totalPages", bookPage.getTotalPages() == 0 ? 1 : bookPage.getTotalPages());
		model.addAttribute("totalElements", bookPage.getTotalElements());
		model.addAttribute("message", message.equals("")?null:message);
		model.addAttribute("error", error.equals("")?null:error);
		return "view/admin/management-book";
	}

	@GetMapping("/new-book")
	public String showFormNewBook(Model model) {
		NewBookDto book = new NewBookDto();
		List<Author> authors = authorService.findAll();
		List<Category> categories = categoryService.findAll();
		model.addAttribute("book", book);
		model.addAttribute("authors", authors);
		model.addAttribute("categories", categories);
		return "view/admin/form-add-new-book";
	}

	@PostMapping("/add-new-book")
	public ModelAndView  addNewBook(@ModelAttribute NewBookDto bookDto, Model model, RedirectAttributes ra) {
		ModelAndView mav = new ModelAndView();
		try {
			Message message = bookService.save(bookDto);
			ra.addFlashAttribute("mesage", message);
			mav.setViewName("redirect:/admin/management-book");
		} 
		catch (ObjectNotFoundException o) {
			mav.setViewName("view/admin/form-add-new-book");
			mav.addObject("book", bookDto);
			mav.addObject("errorMsg",new ErrorMsgDto(o.getMessage()));
			List<Author> authors = authorService.findAll();
			List<Category> categories = categoryService.findAll();
			model.addAttribute("authors", authors);
			model.addAttribute("categories", categories);
		}
		catch (Exception e) {
			mav = new ModelAndView("signin-up/common-error");
			mav.addObject("errorMsg", new ErrorMsgDto("Cannot save!"));
		}
		return mav;
	}
	
	@GetMapping(value = "/management-book-ajax", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String callBookAjax(@RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "5") int pageSize,
			@RequestParam(defaultValue = "id") String sortBy) {
		this.pageNo=pageNo;this.pageSize=pageSize;this.sortBy=sortBy;
		List<DisplayBookDto> books=bookService.findAll(pageNo, pageSize, sortBy);
		int i=1;
		String html="";
		for(DisplayBookDto book: books) {
			html += "<tr>\r\n"
					+ "                  <td>"+(pageNo*pageSize+i)+"</td>"
					+ "                  <td>"
					+ "                    <a href='#'"
					+ "                      ><img"
					+ "                        style='width: 20px'"
					+ "                        src='https://i.pinimg.com/236x/08/44/c5/0844c5eb33e92d674e6ad124bac4903a.jpg'"
					+ "                        class='avatar'"
					+ "                        alt='Avatar'"
					+ "                      />"
					+ "                     "+book.getBookTitle()+"</a"
					+ "                    >"
					+ "                  </td>"
					+ "                  <td>"+book.getDescription()+"</td>"
					+ "                  <td>"+book.getCategory()+"</td>"
					+ "                  <td>"+book.getAuthors()+"</td>"
					+ "                  <td>"+book.getAmountInStock()+"</td>"
					+ "                  <td>"
					+ "                    <span>"+book.getPrice()+"</span>"
					+ "                  </td>"
					+ "                  <td>"
					+ "                    <a"
					+ "                      href='/admin/update-book/"+book.getId()+"'"
					+ "                      class='settings'"
					+ "                      title='Settings'"
					+ "                      data-toggle='tooltip'"
					+ "                      ><i class='material-icons'>&#xE8B8;</i></a"
					+ "                    >"
					+ "                    <a"
					+ "                      href='/admin/delete-book/"+book.getId()+"'"
					+ "                      class='delete'"
					+ "                      title='Delete'"
					+ "                      data-toggle='tooltip'"
					+ "                      ><i class='material-icons'>&#xE5C9;</i></a"
					+ "                    >"
					+ "                  </td>"
					+ "                </tr>";
					i++;
			
		}
		
		return html;
	}
	
	@GetMapping(value = "/delete-book/{id}")
	public ModelAndView deleteBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes ) {
		ModelAndView mav=new ModelAndView();
		mav.setViewName("redirect:/admin/management-book");
		try {
			Message msg= bookService.delete(id);
			redirectAttributes.addFlashAttribute("message", msg.getContent());
		} catch (ObjectNotFoundException o) {
			redirectAttributes.addFlashAttribute("error", o.getMessage());
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Lỗi không xác định");
		}
		return mav;
	}

	@GetMapping(value = "/update-book/{id}")
	public ModelAndView updateBook(@PathVariable("id") Long id, RedirectAttributes ra) {
		ModelAndView mav = new ModelAndView();
		try {
			NewBookDto updateBook = bookService.getUpdateBook(id);
			List<Author> authors = authorService.findAll();
			List<Category> categories = categoryService.findAll();
			mav.addObject("book", updateBook);
			mav.addObject("authors", authors);
			mav.addObject("categories", categories);
			mav.setViewName("view/admin/form-add-new-book");
		} catch (ObjectNotFoundException e) {
			mav.setViewName("redirect:/admin/management-book");
			ra.addFlashAttribute("error", e.getMessage());
		} catch (Exception e) {
			mav.setViewName("view/error/error-page");
		}
		return mav;
	}
	
}












