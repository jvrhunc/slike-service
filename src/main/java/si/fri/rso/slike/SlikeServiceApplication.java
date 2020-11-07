package si.fri.rso.slike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("si.fri.rso.slike.repositories")
public class SlikeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlikeServiceApplication.class, args);
	}

}