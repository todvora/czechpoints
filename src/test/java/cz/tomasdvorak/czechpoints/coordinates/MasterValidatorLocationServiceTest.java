package cz.tomasdvorak.czechpoints.coordinates;

import cz.tomasdvorak.czechpoints.dto.Coordinates;
import cz.tomasdvorak.czechpoints.dto.Czechpoint;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MasterValidatorLocationServiceTest {

    private LocationService master;
    private LocationService validator;
    private LocationService nullService;

    @Before
    public void setUp() throws Exception {
        master = czechpoint -> new Coordinates("", 49.124950, 14.012775, "master");
        validator = czechpoint -> new Coordinates("", 49.117718, 14.001080, "validator");
        nullService = czechpoint -> null;
    }

    @Test
    public void getCoordinatesMasterValidator() throws Exception {
        final MasterValidatorLocationService service = new MasterValidatorLocationService(master, validator);
        final Coordinates coordinates = service.getCoordinates(new Czechpoint());
        Assert.assertEquals("master", coordinates.getDisplayName());
    }

    @Test
    public void getCoordinatesMasterNull() throws Exception {
        final MasterValidatorLocationService service = new MasterValidatorLocationService(master, nullService);
        final Coordinates coordinates = service.getCoordinates(new Czechpoint());
        Assert.assertEquals("master", coordinates.getDisplayName());
    }

    @Test
    public void getCoordinatesNullValidator() throws Exception {
        final MasterValidatorLocationService service = new MasterValidatorLocationService(nullService, validator);
        final Coordinates coordinates = service.getCoordinates(new Czechpoint());
        Assert.assertEquals("validator", coordinates.getDisplayName());
    }
}
