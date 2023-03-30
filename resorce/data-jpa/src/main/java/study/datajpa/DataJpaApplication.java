package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;

//@EnableJpaAuditing(modifyOnCreate = false) // update할때, null - 권장x
@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	//createBy, updateBy

	/**
	 * 실제로는 Spring Security에서 id를 꺼내거나
	 * session으로 꺼냄
	 */
	@Bean
	public AuditorAware<String> auditorProvider() {
		//id값 줌

		return ()->Optional.of(UUID.randomUUID().toString());
	}

}
