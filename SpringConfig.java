package ru.ruselprom.signs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.ruselprom.signs.data.InputData;

@Configuration
@ComponentScan("ru.ruselprom.signs")
@PropertySource("classpath:ru/ruselprom/signs/signatureApp.properties")
public class SpringConfig {
    @Bean
    public String oid() {
        return InputData.getOid();
    }
    @Bean
    public String filePath() {
        return InputData.getFilePath();
    }
}