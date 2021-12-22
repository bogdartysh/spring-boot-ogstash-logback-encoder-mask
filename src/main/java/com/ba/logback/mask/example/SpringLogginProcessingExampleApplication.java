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
    public void consumeFromCoreTopicPartitionZERO() {
        var containers = List.of(
                LogEvent.builder()
                        .email("pershyi@example.com")
                        .inn("3710937752")
                        .pubdatastr("public 1")
                        .build(),
                LogEvent.builder()
                        .email("druhyi.mama+1a@test.example.com")
                        .inn("25914204750123123123")
                        .pubdatastr("public 2")
                        .build()
        );
        for (var c : containers) {
            log.info("got [{}]", c);
        }
    }

    @Builder
    @Data
    public static class LogEvent {
        String email;
        String pubdatastr;
        String inn;
    }

}
