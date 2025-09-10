package ru.practicum.statsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@SpringBootApplication(scanBasePackages = {
//        "ru.practicum.statsserver",
//        "ru.practicum.statsdto"
//})
public class StatsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatsServerApplication.class, args);
    }

}
