package cz.tomasdvorak.czechpoints.providers;

import cz.tomasdvorak.czechpoints.dto.DataPage;

import java.util.List;

public interface PagesProvider {
    List<DataPage> getKnownPages() throws Exception;
}
