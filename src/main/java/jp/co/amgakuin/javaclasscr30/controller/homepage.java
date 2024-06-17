package jp.co.amgakuin.javaclasscr30.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * コントローラクラス
 */
@Controller
public class homePage {
	private String dopost() {
		//@Repuestname
		System.out.printf(name);
		return "homepage";
	}
}
