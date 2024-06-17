package jp.co.amgakuin.javaclasscr30.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
/*トップページコントローラー*/
@Controller
public class TopPage {
	/**/
	@GetMapping("/")
	private String index(Model model) {
		model.addAttribute("message", "Hello World!!");
		return "index";
	}
}
