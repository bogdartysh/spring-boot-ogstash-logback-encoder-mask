package org.ba.logback.mask.example;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@SpringBootApplication
@Slf4j
public class SpringLogginProcessingExampleApplication {
    public static void main(String[] args) {
        var springApp = new SpringApplication(SpringLogginProcessingExampleApplication.class);
       springApp.setAdditionalProfiles("json-nomask");
      //  springApp.setAdditionalProfiles("json-sync");
        springApp.run(args);
    }

    List<PersonLoggable> messages = List.of(
            PersonLoggable.builder()
                    .inn("3710937752")
                    .pubdatastr("public 1")
                    .build(),
            PersonLoggable.builder()
                    .inn("2591420475")
                    .pubdatastr("public 2")
                    .postCode("40231")
                    .build()
    );


    @GetMapping("/msg/{inn}")
    public Optional<PersonLoggable> getMessage(@PathVariable String inn) {
        var resp =  messages.stream().filter(c -> c.inn.equals(inn)).findFirst();
        log.info("RESP IS " + resp);
        return resp;
    }


    @Builder
    @Data
    public static class PersonLoggable {
        String pubdatastr;
        @Nullable
        String postCode;

        //@ToString.Include(name = "postCode")
        //String postCode() {
            return Objects.isNull(postCode) ? postCode : DigestUtils.sha256Hex(postCode);
        }

        String inn;
        //@ToString.Include(name = "inn")
       // String innCode() { return Objects.isNull(postCode) ? inn : DigestUtils.sha256Hex(inn); }
    }

}
