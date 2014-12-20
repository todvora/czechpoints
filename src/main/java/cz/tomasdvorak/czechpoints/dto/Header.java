package cz.tomasdvorak.czechpoints.dto;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Header {
    NAME("Typ úřadu", "Jméno", "Banka"),
    MUNICIPALITY("Obec"),
    STREET("Ulice"),
    CITY("Město"),
    ZIP("PSČ"),
    WWW("WWW");

    private final Set<String> czechLabel;
    private Header(String... czechLabel) {
        this.czechLabel = new HashSet<>(Arrays.asList(czechLabel));
    }

    private Set<String> getCzechLabel() {
        return czechLabel;
    }

    public static Header getByLabel(final String czechLabel) {
        for(Header header : values()) {
            if(header.getCzechLabel().contains(czechLabel)) {
                return header;
            }
        }
        return null;
    }
}
