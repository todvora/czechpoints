package cz.tomasdvorak.czechpoints.persistence;

import cz.tomasdvorak.czechpoints.dto.Czechpoint;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonPersistenceService implements PersistenceService {

    private final Path datastore;
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationConfig.Feature.INDENT_OUTPUT);

    public JsonPersistenceService(final Path pathToDatastore) {
        this.datastore = pathToDatastore;
    }

    @Override
    public synchronized List<Czechpoint> readAll() {
        try {
            return objectMapper.readValue(new FileInputStream(datastore.toFile()), new TypeReference<List<Czechpoint>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void writeAll(final List<Czechpoint> czechpoints) throws IOException {

        final List<Czechpoint> sorted = new ArrayList<>(czechpoints);

        Collections.sort(sorted,
            (Czechpoint o1, Czechpoint o2) -> new CompareToBuilder().append(o1.getZip(), o2.getZip()).append(o1.getCity(), o2.getCity())
                .append(o1.getStreet(), o2.getStreet()).toComparison());

        final StringWriter writer = new StringWriter();
        objectMapper.writeValue(writer, czechpoints);
        final String serialized = writer.toString();
        Files.write(datastore, serialized.getBytes());
    }
}
