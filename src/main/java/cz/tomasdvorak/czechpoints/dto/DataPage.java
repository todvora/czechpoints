package cz.tomasdvorak.czechpoints.dto;

public class DataPage {
    private final String region;
    private final String type;
    private final String url;

    public DataPage(final String region, final String type, final String url) {
        this.region = region;
        this.type = type;
        this.url = url;
    }

    public String getRegion() {
        return region;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}
