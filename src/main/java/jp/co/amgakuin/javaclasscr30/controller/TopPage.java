package jp.co.amgakuin.javaclasscr30.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** 
 * トップページコントローラー
 * @author amg
 * 
 * 
 */

@Controller
public class TopPage {
	
	/**
	 * インデックスページ表示
	 * @param model
	 * @return
	 * 
	 * 
	 */
	@GetMapping("/")
	private String index(Model model) {
		model.addAttribute("message", "Hello World!!");
		return "index";
	}
}
