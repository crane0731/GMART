package gmart.gmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GmartApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmartApplication.class, args);
	}

}
