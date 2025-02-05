package br.com.emersondias.ebd.config;

import br.com.emersondias.ebd.adapters.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Configuration
public class AppConfig {

    @Bean
    public static Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();
    }

    @Bean
    public static RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
