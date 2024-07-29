package clovider.clovider_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableJpaAuditing
public class CloviderBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloviderBeApplication.class, args);
	}

}
