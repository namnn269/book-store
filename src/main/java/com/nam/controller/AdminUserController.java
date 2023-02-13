package com.nam.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
import com.nam.dto.UserDto;
import com.nam.dto.UserRegistrationFormDto;
import com.nam.dto.UserUpdateDto;
import com.nam.entity.Role;
import com.nam.entity.User;
import com.nam.event.RegistrationCompletionEvent;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectAlreadyExistedException;
import com.nam.exception_mesage.ObjectCanNotBeDelete;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.exception_mesage.ValidFormException;
import com.nam.repository.IRoleRepository;
import com.nam.service.IUserService;
import com.nam.utils.Constants;
import com.nam.utils.UrlFromUser;

@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "utf-8")
@RequestMapping(value = "/admin")
public class AdminUserController {

	@Autowired
	private IUserService userService;
	@Autowired
	private ApplicationEventPublisher publisher;
	@Autowired
	private IRoleRepository roleRepo;
	@Autowired
	private Environment env;
	
	private String domain = Constants.DOMAIN;

	/* Trả về trang quản lý người dùng */
	@GetMapping({"/management-user",""})
	public String userManagement(Model model, @ModelAttribute("message") String message,
												@ModelAttribute("error") String error) {
		List<Role> roles= roleRepo.findAll();
		model.addAttribute("roles", roles);
		model.addAttribute("message",message.equals("")?null:message);
		model.addAttribute("error", error.equals("")?null:message);
		return "view/admin/management-user";
	}
	

	/* show form đăng ký thành viên từ admin */
	@GetMapping(value = "/new-user")
	public String showUserForm(Model model) {
		model.addAttribute("user", new UserRegistrationFormDto());
		return "view/admin/form-add-new-user";
	}

	/* lấy obj từ form đăng ký -> đăng ký user hoặc xử lý lỗi */
	@PostMapping(value = "/add-new-user")
	public ModelAndView addNewUser(@ModelAttribute UserRegistrationFormDto userRegDto, 
				HttpServletRequest http, RedirectAttributes ra) {
		ModelAndView mav = new ModelAndView();
		try {
			User user = (User) userService.saveNewRegisterUser(userRegDto).orElseThrow();
			publisher.publishEvent(new RegistrationCompletionEvent(user, http.getLocale(), UrlFromUser.getUrl(http)));
			ra.addFlashAttribute("message", "Thành công!");
			mav.setViewName("redirect:/admin/management-user");
		} catch (ObjectAlreadyExistedException | ValidFormException o) {
			ErrorMsgDto errorMsgDto = new ErrorMsgDto(o.getMessage());
			mav.addObject("errorMsg", errorMsgDto);
			mav.addObject("user", userRegDto);
			mav.setViewName("view/admin/form-add-new-user");
		} catch (Exception e) {
			mav = new ModelAndView("view/error/common-error");
			mav.addObject("errorMsg", new ErrorMsgDto("Cannot save new user!"));
			e.printStackTrace();
		}
		return mav;
	}

	
	/* lấy id user cần update -> show form update */
	@GetMapping("/update-user/{id}")
	public ModelAndView showFormUpdate(@PathVariable("id") Long id, RedirectAttributes ra) {
		ModelAndView mav = new ModelAndView();
		UserUpdateDto userUpdateDto=userService.getUpdateUser(id);
		List<Role> roles=roleRepo.findAll();
		try {
			mav.addObject("user", userUpdateDto);
			mav.addObject("roles", roles);
			mav.setViewName("view/admin/form-update-role-user");
		} catch (ObjectNotFoundException e) {
			ra.addFlashAttribute("error", env.getProperty("message.not.find.user"));
			mav.setViewName("redirect:/admin/management-user");
		}
		return mav;
	}
	
	/* lấy obj từ form -> thực hiện update */
	@PostMapping(value = "/update-user")
	public ModelAndView updateUser(@ModelAttribute UserUpdateDto dto, RedirectAttributes ra) {
		ModelAndView mav = new ModelAndView();
		try {
			Message message = userService.update(dto);
			ra.addFlashAttribute("message", message.getContent());
			mav.setViewName("redirect:/admin/management-user");
		} catch (ObjectNotFoundException e) {
			ra.addFlashAttribute("error", e.getMessage());
			mav.setViewName("redirect:/admin/management-user");
		} catch (Exception e) {
			ra.addFlashAttribute("error", "Lỗi!");
			mav.setViewName("redirect:/admin/management-user");
		}
		return mav;
	}
	
	/* lấy id từ đường dẫn -> thực hiện xóa user */
	@GetMapping(value = "/delete-user", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String deleteUser(@RequestParam("id") Long id, RedirectAttributes ra) {
		Message message;
		try {
			message = userService.delete(id);
		} catch (ObjectNotFoundException o) {
			message = new Message(o.getMessage());
		}  catch (ObjectCanNotBeDelete e) {
			message = new Message(e.getMessage());
		}
		return message.getContent();
	}
	
	/* Trả về 1 mảng HTML
	 * Phần tử 1 chứa HTML thông tin người dùng
	 * Phẩn tử 2 chứa HTML chứa thông tin pagination */
		@GetMapping(value = "/management-user-ajax")
		@ResponseBody
		public String[] callAjaxUser(	@RequestParam(defaultValue = "0", name = "pageNo") int pageNo,
										@RequestParam(defaultValue = "6", name = "pageSize") int pageSize,
										@RequestParam(defaultValue = "", name = "searchFor") String searchFor,
										@RequestParam(defaultValue = "0") Long roleId,
										@RequestParam(defaultValue = "2") int status) {
			List<UserDto> list = userService.findAll(pageNo, pageSize, searchFor, roleId, status);
			int i = 1;
			String html = "";
					for (UserDto userDto : list) {
						
					html +=
					" <tr>"
					+ "                    <td>" + (pageNo * pageSize + i) + " &nbsp; "
					+ "						  <input class='delete-many-input' type='checkbox' value='"+userDto.getId()+"' />"
					+ "					   </td>"
					+ "                    <td>"+userDto.getUsername()+"</td>"
					+ "                    <td>"
					+ "                      <a href='#'"
					+ "                        ><img"
					+ "                          style='width: 20px'"
					+ "                          src='https://i.pinimg.com/236x/08/44/c5/0844c5eb33e92d674e6ad124bac4903a.jpg'"
					+ "                          class='avatar'"
					+ "                          alt='Avatar'/>"
					+ "                        "+userDto.getFullName()+"</a"
					+ "                      >"
					+ "                    </td>"
					+ "                    <td>"+userDto.getEmail()+"</td>"
					+ "                    <td>"+userDto.getRole()+"</td>"
					+ "                    <td>"
					+ "                        <span class='status text-"+ (userDto.isEnabled()?"success":"danger") +"'>&bull;</span> "+(userDto.isEnabled()?"Active":"Suspended")+""
					+ "                    </td>"
					+ "                    <td>"
					+ "                      <a"
					+ "                        href='"+domain+"/admin/update-user/"+userDto.getId()+"'"
					+ "                        class='settings'"
					+ "                        title='Settings'"
					+ "                        data-toggle='tooltip'"
					+ "                        ><i class='material-icons'>&#xE8B8;</i></a"
					+ "                      >"
					+ "                      <button"
					+ "                       value=" + userDto.getId()
					+ "                        class='delete'"
					+ "                        title='Delete'"
					+ "                        data-toggle='tooltip'"
					+ "                        ><i class='material-icons'>&#xE5C9;</i></button>"
					+ "                    </td>"
					+ "                  </tr>";
								i++;
					}
			String paginationHtml = getPaginationString(pageNo, pageSize, searchFor, roleId, status);
			return new String[] {html, paginationHtml};
		}
		
		/* Trả về HTML chứa thông tin pagination user */
		private  String getPaginationString(int pageNo, int pageSize, String searchKey, Long roleId, int status) {
			Page<User> userPage = userService.getPageable(pageNo, pageSize, searchKey, roleId, status);
			String html =			"<div class='hint-text'>"
					+ "                Tổng"
					+ "                <b>"+userPage.getTotalElements()+"</b> thành viên"
					+ "              </div>"
					+ "              <ul class='pagination'>"
					+ "                <li class='previous-page-item'>"
					+ "                  <a href='#' class='page-link'>Previous</a>"
					+ "                </li>";
					
			for (int i = 1; i <= userPage.getTotalPages(); i++) {
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











