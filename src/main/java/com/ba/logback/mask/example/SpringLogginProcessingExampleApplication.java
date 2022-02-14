package org.ba.logback.mask.example;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
@Slf4j
public class SpringLogginProcessingExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringLogginProcessingExampleApplication.class, args);
    }


    @PostConstruct
    public void logMessages() {
        var messages = List.of(
                LogEvent.builder()
                        .inn("3710937752")
                        .pubdatastr("public 1")
                        .build(),
                LogEvent.builder()
                        .inn("2591420475")
                        .pubdatastr("public 2")
                        .build()
        );
        for (var c : messages) {
            log.warn("got [{}]", c);
        }
    }

    @Builder
    @Data
    public static class LogEvent {
        String pubdatastr;
        String inn;
    }
    
    @Builder
    @Data
    public static class Address {
        @ToString.Include(name="street")
        public String addressStreetToString() {
            return toMaskedAddress(street);
        }
        String street;
    }

    
    public static String toMaskedAddress(String str) {
        return toStringFirstCharsOfEachWord(str, 1);
    }
    
        static String toStringFirstCharsOfEachWord(String str, int firstChars) {
        try {
            return !StringUtils.isEmpty(str) && str.length() > firstChars ? (String)Arrays.stream(str.split("\\p{Space}")).map((s) -> {
                return s.length() > firstChars ? s.substring(0, firstChars) + "*****" : s;
            }).collect(Collectors.joining(" ")) : str;
        } catch (Exception var3) {
            LOG.warn("failed to mask [{}]", var3.getMessage(), var3);
            return str;
        }
    }
}
