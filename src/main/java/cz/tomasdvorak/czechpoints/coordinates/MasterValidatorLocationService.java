package cz.tomasdvorak.czechpoints.coordinates;

import cz.tomasdvorak.czechpoints.dto.Coordinates;
import cz.tomasdvorak.czechpoints.dto.Czechpoint;

public class MasterValidatorLocationService implements LocationService {

    private final LocationService master;
    private final LocationService validator;

    public MasterValidatorLocationService(final LocationService master, final LocationService validator) {
        this.master = master;
        this.validator = validator;
    }

    @Override
    public Coordinates getCoordinates(final Czechpoint czechpoint) throws Exception {

        final Coordinates masterCoordinates = master.getCoordinates(czechpoint);
        final Coordinates validatorCoordinates = validator.getCoordinates(czechpoint);

        if (masterCoordinates != null) {
            if (validatorCoordinates != null) {
                return compareAndReturn(masterCoordinates, validatorCoordinates);
            } else {
                return masterCoordinates;
            }
        } else {
            return validatorCoordinates;
        }
    }

    private Coordinates compareAndReturn(final Coordinates masterCoordinates, final Coordinates validatorCoordinates) {
        final double distance = distFrom(masterCoordinates.getLat(), masterCoordinates.getLon(), validatorCoordinates.getLat(),
            validatorCoordinates.getLon());
        if (distance < 2) {
            return masterCoordinates;
        } else {
            return validatorCoordinates;
        }
    }

    private double distFrom(final double lat1, final double lng1, final double lat2, final double lng2) {
        final double earthRadius = 6371; // kilometers
        final double dLat = Math.toRadians(lat2 - lat1);
        final double dLng = Math.toRadians(lng2 - lng1);
        final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (earthRadius * c);
    }
}
