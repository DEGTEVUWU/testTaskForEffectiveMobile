package testtask.testtaskforeffectivemobile.config;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtherConfiguration {
    @Bean
    public static Faker getFaker() {
        return new Faker();
    }
}
