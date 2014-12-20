package cz.tomasdvorak.czechpoints.providers;

import cz.tomasdvorak.czechpoints.dto.DataPage;
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

public class CzechpointPages implements PagesProvider {

    private static final String HOMEPAGE = "http://www.czechpoint.cz";
    public static final String CZECH_POST_TYPE = "Česká pošta";

    @Override
    public List<DataPage> getKnownPages() throws Exception {
        List<DataPage> result = new ArrayList<>();
        final Map<Integer, String> types = getTypes();
        final String html = getHomepageContent();
        Document doc = Jsoup.parse(html);
        final Elements elements = doc.select("#menu1 p.subnavig a");
        for(Element e : elements) {

            final String region = e.attr("title");
            final String url = e.attr("href");
            if(url.contains("node/55")) {
                for(Map.Entry<Integer, String> type : types.entrySet()) {
                    result.add(new DataPage(region,type.getValue(), HOMEPAGE + url.replace("node/55", "node/" + type.getKey())));
                }
            } else {
                result.add(new DataPage(region, "Zastupitelský úřad ČR", HOMEPAGE + url));
            }
        }
        Collections.shuffle(result);
        return result;
    }

    protected String getHomepageContent() throws Exception {
        return URLConnectionReader.getText(HOMEPAGE);
    }

    private Map<Integer, String> getTypes() {
        final Map<Integer, String> types = new HashMap<>();
        types.put(55, "Úřad");
        types.put(57, "Hospodářská komora");
        types.put(56, CZECH_POST_TYPE);
        types.put(148, "Notář");
        types.put(613, "Banka");
        return types;
    }
}
