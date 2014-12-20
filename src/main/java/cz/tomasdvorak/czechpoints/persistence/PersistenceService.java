package cz.tomasdvorak.czechpoints.persistence;

import cz.tomasdvorak.czechpoints.dto.Czechpoint;

import java.io.IOException;
import java.util.List;

public interface PersistenceService {
    List<Czechpoint> readAll();
    void writeAll(List<Czechpoint> czechpoints) throws IOException;
}
