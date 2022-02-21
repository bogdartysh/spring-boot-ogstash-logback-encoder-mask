package org.ba.logback.mask.example;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class TestUtils {

    @SneakyThrows
    public static String getStringFromFile(String filePath) {
        Path path = Paths.get(filePath);
        return Files.readAllLines(path).stream().collect(Collectors.joining(System.lineSeparator()));
    }


    static boolean allSame(Map<Object, Object> expected, Map<Object, Object> actual, Set<String> keys) {
        for (var k : keys) {
            if (ObjectUtils.notEqual(expected.get(k), actual.get(k)))
                return false;
        }
        return true;
    }
}
