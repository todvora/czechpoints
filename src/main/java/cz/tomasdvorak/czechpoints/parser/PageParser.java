package cz.tomasdvorak.czechpoints.parser;

import cz.tomasdvorak.czechpoints.dto.Czechpoint;
import cz.tomasdvorak.czechpoints.dto.DataPage;

import java.util.List;

public interface PageParser {
    List<Czechpoint> readCzechpoints(DataPage page) throws Exception;
}
