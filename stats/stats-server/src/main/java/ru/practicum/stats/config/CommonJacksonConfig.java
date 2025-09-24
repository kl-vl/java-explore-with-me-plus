package ru.practicum.stats.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class CommonJacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat(CommonDateTimeConst.DATE_TIME_PATTERN);

            // LocalDate
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(CommonDateTimeConst.DATE_PATTERN)));
            builder.deserializers(new LocalDateDeserializer(DateTimeFormatter.ofPattern(CommonDateTimeConst.DATE_PATTERN)));

            // LocalDateTime
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(CommonDateTimeConst.DATE_TIME_PATTERN)));
            builder.deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(CommonDateTimeConst.DATE_TIME_PATTERN)));

            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        };
    }
}
