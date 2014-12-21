package cz.tomasdvorak.czechpoints.workflow;

import cz.tomasdvorak.czechpoints.dto.Czechpoint;
import cz.tomasdvorak.czechpoints.persistence.JsonPersistenceService;
import cz.tomasdvorak.czechpoints.persistence.PersistenceService;
import cz.tomasdvorak.czechpoints.utils.IdentificatorUtils;

import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ForeachCzechpoint {
    public static final String DATAFILE = "src/main/resources/czechpoints.json";

    private final PersistenceService persistenceService = new JsonPersistenceService(
            Paths.get(PersistenceService.class.getResource("/").getFile()).getParent().getParent().resolve(DATAFILE)
    );

    private final List<Czechpoint> toPersist = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        new ForeachCzechpoint().start();
    }

    public void start() throws Exception {
        persistenceService.readAll().stream()
                .map(this::process)
                .forEach(toPersist::add);

        persistenceService.writeAll(toPersist);
    }


    private Czechpoint process(final Czechpoint czechpoint) {
        try {
            String hash = IdentificatorUtils.getId(czechpoint);
            czechpoint.setId(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return czechpoint;
    }
}
