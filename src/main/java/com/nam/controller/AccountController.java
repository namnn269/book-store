package com.nam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nam.dto.ProfileUserDto;
import com.nam.dto.PurchasedOrderDetailDto;
import com.nam.entity.ProfileUser;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ValidFormException;
import com.nam.service.IOrderDetailService;
import com.nam.service.IProfileService;

@Controller
@RequestMapping(value = "/account")
public class AccountController {
	// test git 99
	@Autowired
	IProfileService profileService;
	@Autowired
	IOrderDetailService orderDetailService;

	@GetMapping(value = "/my-account")
	public ModelAndView showAccount(@ModelAttribute("message") String message, @ModelAttribute("error") String error) {

		ModelAndView mav = new ModelAndView("view/user/my-account");
		List<PurchasedOrderDetailDto> purchasedBooks=orderDetailService.getListPurchasedOrderDetailDtos();
		ProfileUserDto profileUserDto = new ProfileUserDto();
		try {
			profileUserDto = profileService.getProfileDto();
			mav.addObject("profile", profileUserDto);
			mav.addObject("username", profileUserDto.getUsername());
			mav.addObject("purchased", purchasedBooks);
			mav.addObject("message", message.equals("") ? null : message);
			mav.addObject("error", error.equals("") ? null : error);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@GetMapping(value = "/form-info")
	public ModelAndView showFormUpdateProfile() {
		ModelAndView mav = new ModelAndView("view/user/form-profile");
		try {
			ProfileUser profile = profileService.getProfile();
			mav.addObject("profile", profile);
			mav.addObject("username",profile.getUser().getUsername());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@PostMapping(value = "/update-info")
	public ModelAndView updateProfile(@ModelAttribute ProfileUser profileUser, RedirectAttributes ra) {
		ModelAndView mav = new ModelAndView("redirect:/account/my-account");
		try {
			Message message = profileService.save(profileUser);
			ra.addFlashAttribute("message", message.getContent());
		} catch (ValidFormException f) {
			ra.addFlashAttribute("error", f.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

}
