package cz.tomasdvorak.czechpoints.coordinates;

import cz.tomasdvorak.czechpoints.dto.Coordinates;
import cz.tomasdvorak.czechpoints.dto.Czechpoint;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SeznamCzLocationServiceTest {

    private LocationService service;

    @Before
    public void setUp() throws Exception {
        service = new SeznamCzLocationService() {
            @Override
            protected String constructSearchUrl(final Czechpoint czechpoint) {
                return this.getClass().getResource("/seznamcz.example").toString();
            }
        };
    }

    @Test
    public void getCoordinates() throws Exception {
        final Coordinates coordinates = service.getCoordinates(new Czechpoint());
        Assert.assertEquals(50.0875225506, coordinates.getLat(), 1e-15);
        Assert.assertEquals(14.4351758186, coordinates.getLon(), 1e-15);
        Assert.assertEquals("Hybernská 2086/15, Praha, okres Hlavní město Praha, Česká republika", coordinates.getDisplayName());
    }
}