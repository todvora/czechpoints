package cz.tomasdvorak.czechpoints;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtils {
    public static String getText(final String file) throws IOException {
        final String resource = TestUtils.class.getResource(file).getFile();
        final Path p = Paths.get(resource);
        final List<String> lines = Files.readAllLines(p);
        return lines.stream().collect(Collectors.joining("\n"));
    }
}
