package com.nam.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nam.dto.AdminBookDto;
import com.nam.dto.DisplayBookDto;
import com.nam.dto.ErrorMsgDto;
import com.nam.entity.Author;
import com.nam.entity.Book;
import com.nam.entity.Category;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.service.IAuthorService;
import com.nam.service.IBookService;
import com.nam.service.ICategoryService;
import com.nam.utils.Constants;

@Controller
@ControllerAdvice
@PropertySource(value = "classpath:messages.properties", encoding = "utf-8")
@RequestMapping("/admin")
public class AdminBookController {

	@Autowired
	private IBookService bookService;
	
	@Autowired
	private ICategoryService categoryService;
	
	@Autowired 
	IAuthorService authorService;
	
	@Autowired
	private Environment env;
	
	@Value("${spring.servlet.multipart.max-file-size}")
	private String maxSizeFileUpload;
	
	private String domain = Constants.DOMAIN;
	
	/* Tr??? v??? m??n h??nh qu???n l?? s??ch */
	@GetMapping(value = "/management-book")
	public String bookManagement(Model model, @ModelAttribute("message") String message,
								@ModelAttribute("error") String error) {
		
		List<Category> categories= categoryService.findAll();
		model.addAttribute("categories", categories);
		model.addAttribute("message", message.equals("")?null:message);
		model.addAttribute("error", error.equals("")?null:error);
		return "view/admin/management-book";
	}

	/* Tr??? v??? form ????? th??m m???i 1 cu???n s??ch */
	@GetMapping("/new-book")
	public String showFormNewBook(Model model) {
		AdminBookDto book = new AdminBookDto();
		List<Author> authors = authorService.findAll();
		List<Category> categories = categoryService.findAll();
		model.addAttribute("book", book);
		model.addAttribute("authors", authors);
		model.addAttribute("categories", categories);
		return "view/admin/form-add-new-book";
	}

	/* Th???c hi???n g???i service ????? th??m m???i ho???c c???p nh???t l???i m???t quy???n s??ch n???u s??ch ???? c?? ID */
	@PostMapping("/add-new-book")
	public ModelAndView  addNewBook(	@ModelAttribute AdminBookDto bookDto, 
										@RequestParam(value = "img_upload")MultipartFile multiFile, 
										Model model, 
										RedirectAttributes ra) {
		ModelAndView mav = new ModelAndView();
		try {
			mav.setViewName("redirect:/admin/management-book");
			Message message = bookService.save(bookDto, multiFile);
			ra.addFlashAttribute("message", message.getContent());
		} 
		catch (ObjectNotFoundException o) {
			mav.setViewName("view/admin/form-add-new-book");
			mav.addObject("book", bookDto);
			mav.addObject("errorMsg",new ErrorMsgDto(o.getMessage()));
			List<Author> authors = authorService.findAll();
			List<Category> categories = categoryService.findAll();
			model.addAttribute("authors", authors);
			model.addAttribute("categories", categories);
		} catch (IOException io) {
			mav.addObject("errorMsg", new ErrorMsgDto(io.getMessage()));
		} catch (Exception e) {
			mav = new ModelAndView("view/error/common-error");
			mav.addObject("errorMsg", new ErrorMsgDto("Cannot save!"));
		}
		return mav;
	}
	

	/* Nh???n v??o ID 1 cu???n s??ch v?? g???i service ????? x??a */
	@GetMapping(value = "/delete-book", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String deleteBook(@RequestParam("id") Long id) {
		
		Message message;
		try {
			 message = bookService.delete(id);
		} catch (ObjectNotFoundException o) {
			message=new Message(o.getMessage());
		}catch (Exception e) {
			message=new Message(env.getProperty("message.common.undefined.error"));
		}
		return message.getContent();
	}

	/* Hi???n th??? form c?? ch???a th??ng tin s??ch ????? c???p nh???t s??ch */
	@GetMapping(value = "/update-book/{id}")
	public ModelAndView updateBook(@PathVariable("id") Long id, RedirectAttributes ra) {
		ModelAndView mav = new ModelAndView();
		try {
			AdminBookDto updateBook = bookService.getUpdateBook(id);
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
	
	/* Khi upload ???nh c???a s??ch t??? m??y, n???u x???y ra l???i v?????t qu?? dung l?????ng ???nh 5MB cho ph??p th?? x??? l?? l???i */
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleFileuploadError(RedirectAttributes ra) {
		ra.addFlashAttribute("error", env.getProperty("message.img.over") + maxSizeFileUpload);
		return "redirect:/admin/management-book";
	}
	
	/* Tr??? v??? 1 m???ng HTML
	 * Ph???n t??? 1 ch???a HTML th??ng tin c??c quy???n s??ch
	 * Ph???n t??? 2 ch???a HTML ch???a th??ng tin pagination */
	@GetMapping(value = "/management-book-ajax")
	@ResponseBody
	public String[] callBookAjax(	@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
									@RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
									@RequestParam(value = "searchFor", defaultValue = "") String searchFor,
									@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
									@RequestParam(value = "sortbyPrice", defaultValue = "") String sortByPrice) {
		
		List<DisplayBookDto> books=bookService.findAll(pageNo, pageSize, searchFor, categoryId, sortByPrice);
		int i = 1;
		String html = "";
		for(DisplayBookDto book: books) {
			String bookDes = book.getDescription()==null?"":book.getDescription();
			
			if (bookDes.length() > 100)
				bookDes = bookDes.substring(0, 100) + "...";
			html += "<tr>\r\n"
					+ "                    <td>" + (pageNo * pageSize + i) + " &nbsp; "
					+ "						  <input class='delete-many-input' type='checkbox' value='"+book.getId()+"' />"
					+ "					   </td>"
					+ "                  <td>"
					+ "                    <a href='"+book.getImgLink()+"'"
					+ "                      ><img"
					+ "                        style='height: " + env.getProperty("default.height.book.img.admin") + "'"
					+ "                        src='" + book.getImgLink() + "'"
					+ "                        alt='" + book.getBookTitle() + "'" 
					+ "                      /> <br>"
					+ "                     "+book.getBookTitle()+"</a"
					+ "                    >"
					+ "                  </td>"
					+ "                  <td>"+bookDes+"</td>"
					+ "                  <td>"+book.getCategory()+"</td>"
					+ "                  <td>"+book.getAuthors()+"</td>"
					+ "                  <td>"+book.getAmountInStock()+"</td>"
					+ "                  <td>"
					+ "                    <span>"+book.getPrice()+"??</span>"
					+ "                  </td>"
					+ "                  <td>"
					+ "                    <a"
					+ "                      href='"+domain+"/admin/update-book/"+book.getId()+"'"
					+ "                      class='settings'"
					+ "                      title='Settings'"
					+ "                      data-toggle='tooltip'"
					+ "                      ><i class='material-icons'>&#xE8B8;</i></a"
					+ "                    >"
					+ "                    <button"
					+ "                      value="+book.getId()
					+ "                      class='delete'"
					+ "                      title='Delete'"
					+ "                      data-toggle='tooltip'"
					+ "                      ><i class='material-icons'>&#xE5C9;</i></button>"
					+ "                  </td>"
					+ "                </tr>";
					i++;
			
		}
		
		return new String[] {html, getPaginationString(pageNo, pageSize, searchFor, categoryId, sortByPrice)};
	}
	
	/* Tr??? v??? HTML ch???a th??ng tin pagination s??ch */
	private  String getPaginationString(int pageNo, int pageSize, String searchFor, long categoryId, String sortByPrice) {
		Page<Book> bookPage = bookService.getPageBook(pageNo, pageSize, searchFor, categoryId, sortByPrice);
		String html =			"<div class='hint-text'>"
				+ "                T???ng"
				+ "                <b>"+bookPage.getTotalElements()+"</b> s??ch"
				+ "              </div>"
				+ "              <ul class='pagination'>"
				+ "                <li class='previous-page-item'>"
				+ "                  <a href='#' class='page-link'>Previous</a>"
				+ "                </li>";
				
		for (int i = 1; i <= bookPage.getTotalPages(); i++) {
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












