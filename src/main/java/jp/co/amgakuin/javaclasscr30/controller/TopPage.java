package jp.co.amgakuin.javaclasscr30.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * コントローラクラス
 */
@Controller
public class TopPage {
	
	
	/**
	 * インデックス表示
	 */
	@GetMapping("/")
	private String index(Model model) {
		model.addAttribute("message", "Hello World!!");
		return "index";
	}
}
