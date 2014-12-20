package cz.tomasdvorak.czechpoints.dto;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private String licence;
    private double lat;
    private double lon;
    private String displayName;

    public Coordinates() {
    }

    public Coordinates(final String licence, final double lat, final double lon, final String displayName) {
        this.licence = licence;
        this.lat = lat;
        this.lon = lon;
        this.displayName = displayName;
    }

    public String getLicence() {
        return licence;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setLicence(final String licence) {
        this.licence = licence;
    }

    public void setLat(final double lat) {
        this.lat = lat;
    }

    public void setLon(final double lon) {
        this.lon = lon;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
}
