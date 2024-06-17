package jp.co.amgakuin.javaclasscr30;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * WebApplicationのメインクラス
 * @author amg
 *
 */
@SpringBootApplication
public class WebApplication {

	/**
	 * Springの起動メソッド
	 * @param args 起動時の引数
	 */
	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

}
