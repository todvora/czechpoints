package cz.tomasdvorak.czechpoints.dto;

import java.util.Map;

public class PostApiData {
    private String name;
    private String commune;
    private String street;
    private String postCode;
    private String district;
    private String region;
    private String email;
    private String phone;
    private String fax;
    private double latitude;
    private double longitude;
    private Map<String, String> openingTimes;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(final String commune) {
        this.commune = commune;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(final String postCode) {
        this.postCode = postCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(final String district) {
        this.district = district;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(final String fax) {
        this.fax = fax;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }

    public Map<String, String> getOpeningTimes() {
        return openingTimes;
    }

    public void setOpeningTimes(final Map<String, String> openingTimes) {
        this.openingTimes = openingTimes;
    }
}
