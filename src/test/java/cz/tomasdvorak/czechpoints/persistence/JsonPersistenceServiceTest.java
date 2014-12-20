package cz.tomasdvorak.czechpoints.persistence;

import cz.tomasdvorak.czechpoints.dto.Czechpoint;
import cz.tomasdvorak.czechpoints.dto.DataPage;
import cz.tomasdvorak.czechpoints.parser.CzechpointPageParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JsonPersistenceServiceTest {

    private List<Czechpoint> czechpoints;
    private PersistenceService service;

    @Before
    public void setUp() throws Exception {
        final Path temp = Files.createTempFile(Paths.get("/tmp"), "czechpoints", "json");
        service = new JsonPersistenceService(temp);
        czechpoints = new CzechpointPageParser().readCzechpoints(new DataPage("region", "type", this.getClass().getResource("/listing.example")
                .toString()));
    }

    @Test
    public void readWriteTest() throws Exception {
        service.writeAll(czechpoints);
        final List<Czechpoint> read = service.readAll();
        Assert.assertEquals(czechpoints.size(), read.size());
    }
}
