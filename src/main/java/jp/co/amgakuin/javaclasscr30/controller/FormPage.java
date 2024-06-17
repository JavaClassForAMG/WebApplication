package jp.co.amgakuin.javaclasscr30.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FormPage {
	
	@PostMapping("/")
	private String doPost() {
		return "FormPage";
	}

}
