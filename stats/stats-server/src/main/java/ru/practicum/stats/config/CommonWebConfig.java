package ru.practicum.stats.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class CommonWebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, LocalDateTime.class,
                source -> LocalDateTime.parse(source,
                        DateTimeFormatter.ofPattern(CommonDateTimeConst.DATE_TIME_PATTERN)));
    }
}
