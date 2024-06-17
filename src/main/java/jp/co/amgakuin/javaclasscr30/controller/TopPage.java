package jp.co.amgakuin.javaclasscr30.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * トップページクラス
 * @author amg
 *
 */

@Controller
public class TopPage {
	
	/**
	 * Hallo World！
	 * @param model
	 * @return
	 */
	
	@GetMapping("/")
	private String index(Model model) {
		model.addAttribute("message", "Hello World!!");
		return "index";
	}
}
