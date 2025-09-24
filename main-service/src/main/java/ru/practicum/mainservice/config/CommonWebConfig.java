package ru.practicum.mainservice.config;

import jakarta.annotation.Nullable;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CommonWebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(@Nullable FormatterRegistry registry) {
        if (registry != null) {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setDateTimeFormatter(CommonDateTimeConst.DATE_TIME_FORMATTER);
            registrar.registerFormatters(registry);
        }
    }
}