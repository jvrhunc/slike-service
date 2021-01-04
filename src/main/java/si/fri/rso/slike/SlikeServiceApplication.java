package si.fri.rso.slike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import si.fri.rso.slike.repositories.SlikeRepository;
import si.fri.rso.slike.resolvers.Query;

@SpringBootApplication
@EnableJpaRepositories("si.fri.rso.slike.repositories")
@EnableDiscoveryClient
public class SlikeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlikeServiceApplication.class, args);
	}

	@Bean
	public Query query(SlikeRepository slikeRepository) {
		return new Query(slikeRepository);
	}
}
