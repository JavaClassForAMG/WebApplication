package jp.co.amgakuin.javaclasscr30;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * メインクラス
 * @author amg
 *
 */
@SpringBootApplication
public class WebApplication {
	
	/**
	 * 起動時に実行されるメソッド
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

}
