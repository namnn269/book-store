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
import com.nam.entity.Category;
import com.nam.entity.User;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.service.IBookService;
import com.nam.service.ICategoryService;
import com.nam.service.IUserService;

@Controller
@RequestMapping(value = {"/trang-chu","/"})
public class HomeController {
	@Autowired
	private IBookService bookService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ICategoryService categoryService;

	
	@GetMapping(value = "/test")
	public String test() {
		
		return "view/user/trang-chu";
	}

	@GetMapping(value = { "" })
	public String home(Model model) {
		List<DisplayBookDto> bestSellerBooks=bookService.findBestSellerBooks();
		List<Category> categories= categoryService.findAll();
		
		model.addAttribute("categories", categories);
		model.addAttribute("bestSellerBooks", bestSellerBooks);
		
		User user;
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

	@GetMapping(value = "/product-home-ajax")
	@ResponseBody
	public String[] callProductHomeAjax(
			@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
			@RequestParam(value = "searchFor", defaultValue = "") String searchFor,
			@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
			@RequestParam(value = "sortbyPrice", defaultValue = "") String sortByPrice) {
		List<DisplayBookDto> books = bookService.findAll(pageNo, pageSize, searchFor, categoryId, sortByPrice);

		if(books.isEmpty())
			return new String[] { "<div class='alert alert-warning'>Không tìm thấy sản phẩm nào!</div>"};
		
		StringBuilder html = new StringBuilder("");
		for (DisplayBookDto bookDto : books) {
			html.append(getBookItemHtml(bookDto));
		}
		String paginationHtml = getPaginationHtml(pageNo, pageSize, searchFor, categoryId, sortByPrice);
		return new String[] { html.toString(), paginationHtml };
	}
	
	

	@GetMapping(value = "/book-detail/{id}")
	public ModelAndView showBookDetail(@PathVariable("id") Long id) {
		List<DisplayBookDto> bestSellerBooks=bookService.findBestSellerBooks();
		ModelAndView mav = new ModelAndView("view/user/book-detail");
		mav.addObject("bestSellerBooks", bestSellerBooks);
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


	private String getBookItemHtml(DisplayBookDto bookDto) {
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

	private  String getPaginationHtml(int pageNo, int pageSize, String searchFor, long categoryId, String sortByPrice) {
		Page<Book> bookPage = bookService.getPageBook(pageNo, pageSize, searchFor, categoryId, sortByPrice);
		String html =			"<div class='hint-text'>"
				+ "                Tổng"
				+ "                <b>"+bookPage.getTotalElements()+"</b> sách"
				+ "              </div>"
				+ "              <ul class='pagination'>"
				+ "                <li class='previous-page-item'>"
				+ "                  <a href='#home-search-input' class='page-link'>Previous</a>"
				+ "                </li>";
				
		for (int i = 1; i <= bookPage.getTotalPages(); i++) {
			html += "                  <li class='page-item " + (i == pageNo + 1 ? "active":" ")+"'>"
				+ "                    <a href='#home-search-input' class='page-link'>"+i+"</a>"
				+ "                  </li>";
		}
				
				html+=
				 "                <li class='next-page-item'>"
				+ "                  <a href='#home-search-input' class='page-link'>Next</a>"
				+ "                </li>"
				+ "              </ul>";
		
		return html;
	}
}













