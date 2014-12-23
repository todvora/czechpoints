package cz.tomasdvorak.czechpoints.parser;

import cz.tomasdvorak.czechpoints.dto.Czechpoint;
import cz.tomasdvorak.czechpoints.dto.DataPage;
import cz.tomasdvorak.czechpoints.dto.Header;
import cz.tomasdvorak.czechpoints.utils.URLConnectionReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CzechpointPageParser implements PageParser {

    @Override
    public List<Czechpoint> readCzechpoints(final DataPage page) throws Exception {
        final String html = URLConnectionReader.getText(page.getUrl());
        return parse(html, page);
    }

    private List<Czechpoint> parse(final String html, final DataPage page) {

        final List<Czechpoint> result = new ArrayList<>();
        final Document doc = Jsoup.parse(html);
//        final String region = doc.select(".content h2").text();
        final Elements rows = doc.select("table.prehled tr");
        if (rows.size() < 2) {
            return Collections.emptyList();
        }

        final Map<Integer, Header> headers = getHeaderPositions(rows);

        for (int j = 1; j < rows.size(); j++) {
            final Element e = rows.get(j);
            final Elements cols = e.select("td");
            final Map<Header, String> entry = new HashMap<>();
            for(int i = 0; i < cols.size(); i++) {
                final Element col = cols.get(i);
                if(col.select("a").size() > 0) {
                    final String href = col.select("a").get(0).attr("href");
                    if(!href.trim().isEmpty()) {
                        entry.put(headers.get(i), href);
                    }
                } else {
                    entry.put(headers.get(i), col.text());
                }
            }
            result.add(translate(entry, page));
        }

        return result;
    }

    private Czechpoint translate(final Map<Header, String> entry, final DataPage page) {
        final String street = entry.get(Header.STREET);
        final String city = entry.get(Header.CITY);
        String zip = entry.get(Header.ZIP);
        if(zip != null) {
            zip = zip.replace(" ", "");
        }

        final Czechpoint czechpoint = new Czechpoint(
                entry.get(Header.NAME),
                entry.get(Header.MUNICIPALITY),
                street,
                city,
                zip,
                entry.get(Header.WWW)
        );
        czechpoint.setRegion(page.getRegion());
        czechpoint.setType(page.getType());
        return czechpoint;
    }

    private Map<Integer, Header> getHeaderPositions(final Elements rows) {
        final Map<Integer, Header> headerPositions = new HashMap<>();
        final Element headers = rows.get(0);
        final Elements ths = headers.select("th");
        for (int i = 0; i < ths.size(); i++) {
            final Element header = ths.get(i);
            final String text = header.text();
            final Header colHeader = Header.getByLabel(text);
            headerPositions.put(i, colHeader);
        }
        return headerPositions;
    }
}
