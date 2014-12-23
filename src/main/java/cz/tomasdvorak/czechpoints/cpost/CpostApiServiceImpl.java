package cz.tomasdvorak.czechpoints.cpost;

import cz.tomasdvorak.czechpoints.dto.PostApiData;
import cz.tomasdvorak.czechpoints.utils.URLConnectionReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

public class CpostApiServiceImpl implements CpostApiService {

    @Override
    public PostApiData getByZip(final String zip) throws Exception {
        final long nextAllowedTime = lastQueryTime + DELAY;
        final long now = System.currentTimeMillis();
        if (nextAllowedTime > now) {
            Thread.sleep(nextAllowedTime - now);
        }
        final String response = getWebResponse(zip);
        lastQueryTime = System.currentTimeMillis();
        return parse(response);
    }

    private static final int DELAY = 3000;
    private long lastQueryTime;

    private PostApiData parse(final String response) {
        final Document document = Jsoup.parse(response);
        final Elements items = document.select("postOfficeInformation");
        if (items.size() > 0) {
            final Element xml = items.get(0);
            final PostApiData result = new PostApiData();
            result.setName(getValue(xml, "attributes name"));
            result.setCommune(getValue(xml, "attributes commune"));
            result.setStreet(getStreet(xml));
            result.setPostCode(getValue(xml, "attributes postCode"));
            result.setDistrict(getValue(xml, "attributes district"));
            result.setRegion(getValue(xml, "attributes region"));
            result.setEmail(getValue(xml, "attributes email"));
            result.setPhone(getValue(xml, "attributes phone"));
            result.setFax(getValue(xml, "attributes fax"));
            result.setLatitude(Double.parseDouble(getValue(xml, "attributes latitude")));
            result.setLongitude(Double.parseDouble(getValue(xml, "attributes longitude")));
            result.setOpeningTimes(getOpeningTimes(xml));
            return result;
        }
        return null;
    }

    private Map<String, String> getOpeningTimes(final Element xml) {

//        <openingHours>
//        <day name="monday">
//        <since1>08:00</since1>
//        <to1>10:30</to1>
//        <since2>13:00</since2>
//        <to2>15:30</to2>
//        <since3>15:45</since3>
//        <to3>16:45</to3>
//        </day>
//        <day name="tuesday">
//        <since1>08:00</since1>
//        <to1>10:30</to1>
//        <since2>13:00</since2>
//        <to2>15:30</to2>
//        <since3>15:45</since3>
//        <to3>16:45</to3>
//        </day>
//        <day name="wednesday">
//        <since1>08:00</since1>
//        <to1>10:30</to1>
//        <since2>13:00</since2>
//        <to2>15:30</to2>
//        <since3>15:45</since3>
//        <to3>16:45</to3>
//        </day>
//        <day name="thursday">
//        <since1>08:00</since1>
//        <to1>10:30</to1>
//        <since2>13:00</since2>
//        <to2>15:30</to2>
//        <since3>15:45</since3>
//        <to3>16:45</to3>
//        </day>
//        <day name="friday">
//        <since1>08:00</since1>
//        <to1>10:30</to1>
//        <since2>13:00</since2>
//        <to2>15:30</to2>
//        <since3>15:45</since3>
//        <to3>16:45</to3>
//        </day>
//        <day name="saturday"/>
//        <day name="sunday"/>
//        </openingHours>

        final Elements days = xml.select("openingHours day");
        final Map<String, String> result = new LinkedHashMap<>();
        days.forEach(element -> {
            final String times = parseTimes(element);
            result.put(element.attr("name"), times);
        });
        return result;
    }

    private String parseTimes(final Element element) {
        final StringJoiner joiner = new StringJoiner(", ");
        for (int i = 1; i <= 3; i++) {

            final Elements from = element.select("since" + i);
            final Elements till = element.select("to" + i);

            if(!from.isEmpty() && !till.isEmpty()) {
                joiner.add(from.text() + "-" + till.text());
            }

        }

        final String result = joiner.toString();
        if(result.isEmpty()) {
            return null;
        }
        return result;

    }

    private String getStreet(final Element xml) {
        final String street = getValue(xml, "attributes street");
        final String houseNumber = getValue(xml, "attributes houseNumber");
        final String houseNumberLetter = getValue(xml, "attributes houseNumberLetter");
        final String orientationNumber = getValue(xml, "attributes orientationNumber");
        final String orientationNumberLetter = getValue(xml, "attributes orientationNumberLetter");

        String result = street;
        if (houseNumberLetter != null) {
            result += " " + houseNumber + houseNumberLetter + "/";
        } else {
            result += " " + houseNumber + "/";
        }

        if (orientationNumberLetter != null) {
            result += orientationNumber + orientationNumberLetter;
        } else {
            result += orientationNumber;
        }

        return result;
    }

    private String getValue(final Element xml, final String selector) {
        final Elements select = xml.select(selector);
        if (select.size() > 0) {
            return select.get(0).text();
        }
        return null;
    }

    private String getWebResponse(final String zip) throws Exception {
        return URLConnectionReader.getText(constructSearchUrl(zip));
    }

    protected String constructSearchUrl(final String zip) {
        return "https://b2c.cpost.cz/services/PostOfficeInformation/getDataAsXml?postCode=" + zip;
    }
}
