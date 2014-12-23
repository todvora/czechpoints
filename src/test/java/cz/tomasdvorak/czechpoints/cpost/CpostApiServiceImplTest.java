package cz.tomasdvorak.czechpoints.cpost;

import cz.tomasdvorak.czechpoints.dto.PostApiData;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class CpostApiServiceImplTest {

    private CpostApiServiceImpl getService(final String filename) {
        return new CpostApiServiceImpl() {
            @Override
            protected String constructSearchUrl(final String zip) {
                return this.getClass().getResource(filename).toString();
            }
        };
    }

    @Test
    public void getByZipSimpleData() throws Exception {
        final CpostApiServiceImpl service = getService("/cpostapi.example");
        final PostApiData result = service.getByZip("35002");
        Assert.assertEquals("Cheb 2", result.getName());
        Assert.assertEquals("Cheb", result.getCommune());
        Assert.assertEquals("Riegerova 1302/56", result.getStreet());
        Assert.assertEquals("35002", result.getPostCode());
        Assert.assertEquals("Cheb", result.getDistrict());
        Assert.assertEquals("Karlovarský", result.getRegion());
        Assert.assertEquals("posta.p303028@cpost.cz", result.getEmail());
        Assert.assertEquals("354473387", result.getPhone());
        Assert.assertEquals("354473389", result.getFax());
        Assert.assertEquals(50.072934, result.getLatitude(), 1e-15);
        Assert.assertEquals(12.379387, result.getLongitude(), 1e-15);
    }
    @Test
    public void getByZipMultipleTimes() throws Exception {
        final CpostApiServiceImpl service = getService("/cpostapi_times.example");
        final PostApiData result = service.getByZip("28403");
        Assert.assertEquals("08:00-10:30, 13:00-15:30, 15:45-16:45", result.getOpeningTimes().get("monday"));
        Assert.assertEquals("08:00-10:30, 13:00-15:30, 15:45-16:45", result.getOpeningTimes().get("tuesday"));
        Assert.assertEquals("08:00-10:30, 13:00-15:30, 15:45-16:45", result.getOpeningTimes().get("wednesday"));
        Assert.assertEquals("08:00-10:30, 13:00-15:30, 15:45-16:45", result.getOpeningTimes().get("thursday"));
        Assert.assertEquals("08:00-10:30, 13:00-15:30, 15:45-16:45", result.getOpeningTimes().get("friday"));
        Assert.assertNull(result.getOpeningTimes().get("saturday"));
        Assert.assertNull(result.getOpeningTimes().get("sunday"));
    }
}
