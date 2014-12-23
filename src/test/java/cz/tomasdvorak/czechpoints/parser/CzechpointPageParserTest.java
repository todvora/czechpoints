package cz.tomasdvorak.czechpoints.parser;

import cz.tomasdvorak.czechpoints.dto.Czechpoint;
import cz.tomasdvorak.czechpoints.dto.DataPage;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class CzechpointPageParserTest {

    @Test
    public void parse() throws Exception {

        final List<Czechpoint> parsed = new CzechpointPageParser().readCzechpoints(new DataPage("Karlovarský kraj", "Úřad", this.getClass().getResource("/listing.example").toString()));
        Assert.assertEquals(128, parsed.size());

        final Optional<Czechpoint> any = parsed.stream().filter(czechpoint -> "Dolní Žandov".equals(czechpoint.getCity())).findAny();
        Assert.assertTrue(any.isPresent());

        final Czechpoint one = any.get();
        Assert.assertEquals("35493", one.getZip());
        Assert.assertEquals("Dolní Žandov", one.getCity());
        Assert.assertEquals("č.p. 36", one.getStreet());
        Assert.assertEquals("Dolní Žandov", one.getMunicipality());
        Assert.assertEquals("Karlovarský kraj", one.getRegion());
        Assert.assertEquals("Úřad", one.getType());
        Assert.assertEquals("Obecní úřad", one.getName());
    }


}