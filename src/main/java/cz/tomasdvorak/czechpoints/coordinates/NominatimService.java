package cz.tomasdvorak.czechpoints.coordinates;

import cz.tomasdvorak.czechpoints.dto.Coordinates;
import cz.tomasdvorak.czechpoints.dto.Czechpoint;
import cz.tomasdvorak.czechpoints.utils.URLConnectionReader;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.security.x509.URIName;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NominatimService implements LocationService {

    private static final int DELAY = 5000;
    private long lastQueryTime;

    @Override
    public Coordinates getCoordinates(final Czechpoint czechpoint) throws Exception {
        final long nextAllowedTime = lastQueryTime + DELAY;
        final long now = System.currentTimeMillis();
        if(nextAllowedTime > now) {
            Thread.sleep(nextAllowedTime - now);
        }
        final String nominatimResponse = getNominatimResponse(czechpoint.getStreet(), czechpoint.getCity(), czechpoint.getZip());
        lastQueryTime = System.currentTimeMillis();
        JSONArray results =  new JSONArray(nominatimResponse);
        for(int i = 0; i < results.length(); i++) {
            final JSONObject result = (JSONObject) results.get(i);
            final Coordinates parsed = parse(result);
            if(true) { // TODO: how to choose correct one?
                return parsed;
            }
        }
        return null;
    }

    private Coordinates parse(final JSONObject entry) {
        return new Coordinates(
            entry.getString("licence"),
            entry.getDouble("lat"),
            entry.getDouble("lon"),
            entry.getString("display_name")
        );
    }

    protected String getNominatimResponse(final String street, final String city, final String zip) throws Exception {
        return URLConnectionReader.getText(constructNominatimUrl(street, city, zip));
    }

    private String constructNominatimUrl(final String street, final String city, final String zip) throws UnsupportedEncodingException {

        return String.format("http://nominatim.openstreetmap.org/search.php?format=json&street=%s&city=%s&country=%s&countrycodes=cz",
                URLEncoder.encode(street, "UTF-8"),
                URLEncoder.encode(city, "UTF-8"),
                URLEncoder.encode("Česká Republika", "UTF-8"));
    }
}
