package cz.tomasdvorak.czechpoints.cpost;

import cz.tomasdvorak.czechpoints.dto.PostApiData;

public interface CpostApiService {
    PostApiData getByZip(String zip) throws Exception;
}
