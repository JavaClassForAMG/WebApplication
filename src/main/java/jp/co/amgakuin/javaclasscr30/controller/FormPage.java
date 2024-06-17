package jp.co.amgakuin.javaclasscr30.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FormPage {

	// 画面内のボタンを踏んで行くことをポストというらしいので名前はdoPost
	// 逆にURLから行くのはGet
	// "/"が呼ばれたら舌の関数を呼ぶよって感じ
	@PostMapping("/")
	private String doPost() {
		return "FormPage";
	}
}
