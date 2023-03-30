package hello.itemservice;

import hello.itemservice.web.validation.ItemValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
//implements WebMvcConfigurer
public class ItemServiceApplication   {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}
//
//	//모두 컨트롤러 적용(검증)
//	//@Validated은 있어야함
//	@Override
//	public Validator getValidator() {
//		return new ItemValidator();
//	}
}
