package jp.co.amgakuin.javaclasscr30.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FormPage {
	
	@PostMapping("/")
	private String doPost
	(
		//引数
		Model model,
		@RequestParam("username")String username
	) 
	{
		//本文
		//System.out.println(name);
		model.addAttribute("name",username);
		return "FormPage";
	} 
}
