package com.nam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nam.dto.DisplayBookDto;
import com.nam.entity.Book;
import com.nam.entity.User;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.service.IBookService;
import com.nam.service.IUserService;

@Controller
@RequestMapping(value = {"/trang-chu","/"})
public class HomeController {
	@Autowired
	private IBookService bookService;
	@Autowired
	private IUserService userService;
	private int showQuantity=3;

	
	@GetMapping(value = "/test")
	public String test() {
		
		return "view/user/trang-chu";
	}

	@GetMapping(value = { "" })
	public String home(Model model) {
		User user;
		Page<Book> page = bookService.getPageBook(0, this.showQuantity, "id");
		int totalPages = page.getTotalPages();
		model.addAttribute("totalPages", totalPages==0?1:totalPages);
		try {
			user = userService.getCurrentLoggedInUser();
			model.addAttribute("username", user.getUsername());
		} catch (Exception e) {
			user = null;
		}
		return "view/user/shop-product-list";
	}

	@GetMapping(value = { "/error-page" })
	public String errorPage() {
		return "view/error/error-page";
	}

	@GetMapping(value = "/trang-chu/product-home-ajax", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String callProductHomeAjax(
			@RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "1") int pageSize,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "") String searchKey) {
		List<DisplayBookDto> pageBook;
		if (searchKey.equals("")) {
			pageBook = bookService.findAll(pageNo, pageSize, sortBy);
		} else {
			pageBook = bookService.findAllBySearchKey(sortBy, searchKey);
		}
		StringBuilder html = new StringBuilder("");
		for (DisplayBookDto bookDto : pageBook) {
			html.append(getHtml(bookDto));
		}
		return html.toString();
	}

	@GetMapping(value = "/trang-chu/product-search-home-ajax", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String callSearchProductHomeAjax(
						@RequestParam(defaultValue = "id") String sortBy,
						@RequestParam(defaultValue = "") String searchKey) {
		List<DisplayBookDto> bookDtos = bookService.findAllBySearchKey(sortBy, searchKey);
		StringBuilder html = new StringBuilder("");
		for (DisplayBookDto bookDto : bookDtos) {
			html.append(getHtml(bookDto));
		}
		if(bookDtos.isEmpty())html.append("<div class='alert alert-warning'>Không tìm thấy sản phẩm nào!</div>");
		return html.toString();
	}
	
	@GetMapping(value = "/trang-chu/show-book-by-category-ajax", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String callSearchProductHomeAjax(
						@RequestParam(defaultValue = "1",value = "categoryId") Long categoryId,
						@RequestParam(defaultValue = "id",value = "sortBy") String sortBy) {
		List<DisplayBookDto> bookDtos = bookService.findByCategory(categoryId,sortBy);;
		
		StringBuilder html = new StringBuilder("");
		for (DisplayBookDto bookDto : bookDtos) {
			html.append(getHtml(bookDto));
		}
		if(bookDtos.isEmpty())html.append("<div class='alert alert-warning'>Không tìm thấy sản phẩm nào!</div>");
		return html.toString();
	}
	
	@GetMapping(value = "/trang-chu/change-show-quantity", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public void changeShowQuantityAjax(
			@RequestParam(defaultValue = "3", value = "quantity") int quantity) {
		this.showQuantity=quantity;
	}

	@GetMapping(value = "/trang-chu/book-detail/{id}")
	public ModelAndView showBookDetail(@PathVariable("id") Long id) {
		ModelAndView mav = new ModelAndView("view/user/book-detail");
		User user=null;
		try {
			DisplayBookDto bookDto = bookService.findBookDetailById(id);
			mav.addObject("book", bookDto);
			user = userService.getCurrentLoggedInUser();
			if(user!=null) mav.addObject("username", user.getUsername());
		} catch (ObjectNotFoundException o) {
			mav.addObject("error_book_not_found", o.getMessage());
		} catch (Exception e) {
			mav.addObject("error_book_not_found", "Some thing went wrong!");
			e.printStackTrace();
		}
		return mav;
	}

	@GetMapping(value = "/trang-chu/cart")
	public ModelAndView showShoppingCart() {
		ModelAndView mav = new ModelAndView("/view/user/shopping-cart");

		return mav;
	}

	private String getHtml(DisplayBookDto bookDto) {
		String linkDetail = "/trang-chu/book-detail/" + bookDto.getId() + "";
		String authors = "";
		for (String author : bookDto.getAuthors()) {
			authors += "<br>- " + author;
		}
		String html="<div class='col-md-4 col-sm-6 col-xs-12'>"
				+ "                <div class='product-item'>"
				+ "                  <div class='pi-img-wrapper'>"
				+ "                    <img"
				+ "                      src='"+bookDto.getImgLink()+"'"
				+ "                      class='img-responsive'"
				+ "                      alt='Berry Lace Dress'"
				+ "                    />"
				+ "                    <div>"
				+ "                      <a"
				+ "                        href='"+linkDetail+"'"
				+ "                        > <img src='"+bookDto.getImgLink()+"' class='img-responsive'/> "
				+ "                      </a>"
				+ "                    </div>"
				+ "                  </div>"
				+ "                  <h3>"
				+ "                    <a href='"+"'>"+bookDto.getBookTitle()+"</a>"
				+ "                  </h3>"
				+ "                  <div>Thể loại: "+bookDto.getCategory()+"</div>"
				+ "                  <br>"
				+ "                  <div>Tác giả: "+authors+"</div>"
				+ "                  <br></br>"
				+ "                  <div class='pi-price'>$"+bookDto.getPrice()+"</div>"
				+ "                  </div>"
				+ "                </div>"
				+ "              </div>";
		return html;
	}
	
}













