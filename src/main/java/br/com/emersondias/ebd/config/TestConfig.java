package br.com.emersondias.ebd.config;

import br.com.emersondias.ebd.services.dev.TestDataInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"test"})
@RequiredArgsConstructor
public class TestConfig {

    private final TestDataInitializer testDataInitializer;

    @Bean
    public boolean instantiateDataBase() {
        testDataInitializer.initializeTestData();
        return true;
    }
}
