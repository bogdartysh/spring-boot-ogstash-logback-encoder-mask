package org.ba.logback.mask.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static org.ba.logback.mask.example.TestUtils.getStringFromFile;

@Slf4j
@ActiveProfiles({"test", "json-async"})
@SpringBootTest
@DirtiesContext
public class MaskLoggingTestAsync {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    ObjectMapper objectMapper = new ObjectMapper();

    Logger logger;

    @SneakyThrows
    @BeforeEach
    public void beforeEach() {
        try {
            System.setOut(new PrintStream(outContent));
            logger = LogManager.getLogger(MaskLoggingTestAsync.class);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
            "src/test/resources/skip/msg.txt,src/test/resources/skip/expected_logged_mask_logs.json",
            "src/test/resources/M/msg.txt,src/test/resources/M/expected_logged_mask_logs.json",
            "src/test/resources/L/msg.txt,src/test/resources/L/expected_logged_mask_logs.json",
            "src/test/resources/XL/msg.txt,src/test/resources/XL/expected_logged_mask_logs.json",
            "src/test/resources/XXL/msg.txt,src/test/resources/XXL/expected_logged_mask_logs.json",

    })
    void testLoggingFields(String tobeLoggedPath, String expectedLoggedPath) {
        for (int i = 0; i < 10; i++) {

            //given
            var toBeLogged = getStringFromFile(tobeLoggedPath);
            var expectedLogged = objectMapper.readValue(TestUtils.getStringFromFile(expectedLoggedPath), Map.class);


            //when
            logger.log(Level.ERROR, toBeLogged);

        }

    }
}
