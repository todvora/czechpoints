package cz.tomasdvorak.czechpoints.coordinates;

import cz.tomasdvorak.czechpoints.TestUtils;
import cz.tomasdvorak.czechpoints.dto.Coordinates;
import cz.tomasdvorak.czechpoints.dto.Czechpoint;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NominatimServiceTest {

    private LocationService service;
    @Before
    public void setUp() throws Exception {
        this.service = new NominatimService() {
            @Override
            protected String getNominatimResponse(final String street, final String city, final String zip) throws Exception {
                return TestUtils.getText("/nominatim.example");
            }
        };
    }

    @Test
    public void getCoordinates() throws Exception {
        final Coordinates coordinates = this.service.getCoordinates(new Czechpoint());
        Assert.assertEquals("Data © OpenStreetMap contributors, ODbL 1.0. http://www.openstreetmap.org/copyright", coordinates.getLicence());
        Assert.assertEquals("2423/15, Šlikova, Cheb, Cheb District, Karlovarský kraj, Severozápad, 35002, Czech Republic", coordinates.getDisplayName());
        Assert.assertEquals(50.0774505, coordinates.getLat(), 1e-15);
        Assert.assertEquals(12.3707062, coordinates.getLon(), 1e-15);
    }
}