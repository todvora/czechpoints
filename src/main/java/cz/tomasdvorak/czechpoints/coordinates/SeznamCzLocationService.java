package cz.tomasdvorak.czechpoints.coordinates;

import cz.tomasdvorak.czechpoints.dto.Coordinates;
import cz.tomasdvorak.czechpoints.dto.Czechpoint;
import cz.tomasdvorak.czechpoints.utils.URLConnectionReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SeznamCzLocationService implements LocationService {

    private static final int DELAY = 3000;
    private long lastQueryTime;

    @Override
    public Coordinates getCoordinates(final Czechpoint czechpoint) throws Exception {
        final long nextAllowedTime = lastQueryTime + DELAY;
        final long now = System.currentTimeMillis();
        if(nextAllowedTime > now) {
            Thread.sleep(nextAllowedTime - now);
        }
        final String seznamResponse = getSeznamResponse(czechpoint);
        lastQueryTime = System.currentTimeMillis();
        return parse(seznamResponse);
    }

    private Coordinates parse(final String seznamResponse) {
        final Document document = Jsoup.parse(seznamResponse);
        final Elements items = document.select("item[source=addr]");
        if(items.size() > 0) {
            final Element item = items.get(0);
            return new Coordinates(
                    "© Seznam.cz, a.s. - mapy.cz",
                    Double.parseDouble(item.attr("y")),
                    Double.parseDouble(item.attr("x")),
                    item.attr("title")
            );
        }
        return null;
    }

    protected String getSeznamResponse(final Czechpoint czechpoint) throws Exception {
        return URLConnectionReader.getText(constructSearchUrl(czechpoint.getStreet(), czechpoint.getCity(), czechpoint.getZip()));
    }

    protected String constructSearchUrl(final String street, final String city, final String zip) throws UnsupportedEncodingException {
        return "http://api4.mapy.cz/geocode?query=" + URLEncoder.encode(street + ", " + city, "UTF-8");
    }
}
