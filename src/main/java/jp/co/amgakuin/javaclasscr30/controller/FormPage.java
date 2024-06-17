package jp.co.amgakuin.javaclasscr30.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FormPage {
	
	@PostMapping("/")
	private String doPost(
			Model model,
			@RequestParam("username") String username
			) 
	{
		model.addAttribute("name", username);
		return "FormPage";
	}
}
