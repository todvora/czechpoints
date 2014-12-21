package cz.tomasdvorak.czechpoints.dto;

import java.io.Serializable;
import java.util.Map;

public class Czechpoint implements Serializable {

    private String name;
    private String municipality;
    private String street;
    private String city;
    private String zip;
    private String www;
    private String phone;
    private String fax;
    private Coordinates location;
    private String region;
    private String type;
    private String email;
    private Map<String, String> openingTimes;
    private String id;

    public Czechpoint() {
    }

    public Czechpoint(final String name, final String municipality, final String street, final String city, final String zip,
            final String www, final Coordinates location) {
        this.name = name;
        this.municipality = municipality;
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.www = www;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getMunicipality() {
        return municipality;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public String getWww() {
        return www;
    }

    public Coordinates getLocation() {
        return location;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setMunicipality(final String municipality) {
        this.municipality = municipality;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public void setZip(final String zip) {
        this.zip = zip;
    }

    public void setWww(final String www) {
        this.www = www;
    }

    public void setLocation(final Coordinates location) {
        this.location = location;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
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

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public Map<String, String> getOpeningTimes() {
        return openingTimes;
    }

    public void setOpeningTimes(final Map<String, String> openingTimes) {
        this.openingTimes = openingTimes;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
