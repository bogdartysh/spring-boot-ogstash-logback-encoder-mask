package org.ba.logback.mask.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Objects;
import java.util.Set;

import static org.ba.logback.mask.example.TestUtils.allSame;
import static org.ba.logback.mask.example.TestUtils.getStringFromFile;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ActiveProfiles({"test", "json-sync"})
@SpringBootTest
@DirtiesContext
public class MaskLoggingTest {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    ObjectMapper objectMapper = new ObjectMapper();

    Logger logger;

    @SneakyThrows
    @BeforeEach
    public void beforeEach() {
        try {
            System.setOut(new PrintStream(outContent));
            logger = LogManager.getLogger(MaskLoggingTest.class);
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
    void testLoggingFieldsMsg(String tobeLoggedPath, String expectedLoggedPath) {
        for (int i = 0; i < 10; i++) {
            //given
            var toBeLogged = getStringFromFile(tobeLoggedPath);

            //when
            logger.log(Level.ERROR, toBeLogged);

            //then
            var msgOk = false;
            var expectedLogged = objectMapper.readValue(getStringFromFile(expectedLoggedPath), Map.class);
            for (var line : outContent.toString().split(System.getProperty("line.separator"))) {
                if (StringUtils.isNotBlank(line)) {
                    var readValue = objectMapper.readValue(line, Map.class);
                    if (Objects.equals(Level.ERROR.name(), readValue.get("level"))) {
                        msgOk = msgOk || allSame(expectedLogged, readValue, Set.of("app", "logger", "class", "method", "file"));
                    }
                }
            }
            assertTrue(msgOk, "message not found");
        }
    }


}
