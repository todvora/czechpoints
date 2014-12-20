package cz.tomasdvorak.czechpoints.coordinates;

import cz.tomasdvorak.czechpoints.dto.Coordinates;
import cz.tomasdvorak.czechpoints.dto.Czechpoint;

public interface LocationService {
    Coordinates getCoordinates(Czechpoint czechpoint) throws Exception;
}
