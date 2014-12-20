package cz.tomasdvorak.czechpoints.coordinates;

import cz.tomasdvorak.czechpoints.dto.Coordinates;
import cz.tomasdvorak.czechpoints.dto.Czechpoint;
import cz.tomasdvorak.czechpoints.persistence.PersistenceService;

import java.util.List;
import java.util.Optional;

public class CachingLocationService implements LocationService {

    private final LocationService delegate;
    private final List<Czechpoint> knownCzechpoints;

    public CachingLocationService(final LocationService delegate, final PersistenceService czechpoints) {
        this.delegate = delegate;
        this.knownCzechpoints = czechpoints.readAll();
    }

    @Override
    public Coordinates getCoordinates(final Czechpoint current) throws Exception {

        final Optional<Czechpoint> one = findOne(current);
        if (one.isPresent()) {
            final Coordinates currentLocation = one.get().getLocation();
            if (currentLocation != null) {
                return currentLocation;
            }
        }
        return delegate.getCoordinates(current);
    }

    private Optional<Czechpoint> findOne(final Czechpoint current) {
        return knownCzechpoints
            .stream()
            .filter(czechpoint ->
                    sameStreet(current, czechpoint) &&
                    sameCity(current, czechpoint) &&
                    sameZip(current, czechpoint)
            ).findFirst();
    }

    private boolean sameZip(final Czechpoint current, final Czechpoint czechpoint) {
        return equals(current.getZip(), czechpoint.getZip());
    }

    private boolean sameCity(final Czechpoint current, final Czechpoint czechpoint) {
        return equals(current.getCity(), czechpoint.getCity());
    }

    private boolean sameStreet(final Czechpoint current, final Czechpoint czechpoint) {
        return equals(current.getStreet(), czechpoint.getStreet());
    }

    private boolean equals(final String one, final String two) {
        return one != null && one.equals(two) || two == null;
    }
}
