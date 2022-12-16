package com.nam.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/trang-chu")
public class TestFile {
	
	@GetMapping("/form-upload")
	public String test() {
		//"D:\\D\\A\\Eclipse Workplace 09\\book_store\\src\\main\\resources\\static\\upload"
		return "view/user/trang-chu";
	}
	
	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile multiFile, Model model) {
		String dirBase="D:\\D\\A\\Eclipse Workplace 09\\book_store\\src\\main\\resources\\static\\upload\\";
		try {
			multiFile.transferTo(new File(dirBase+multiFile.getOriginalFilename()));
			String fileLink="/upload/"+multiFile.getOriginalFilename();
			model.addAttribute("linkImg", fileLink);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "view/user/trang-chu";
	}
}
