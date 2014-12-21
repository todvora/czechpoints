package cz.tomasdvorak.czechpoints.utils;

import cz.tomasdvorak.czechpoints.dto.Czechpoint;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IdentificatorUtils {
    public static String getId(Czechpoint czechpoint) throws NoSuchAlgorithmException {
        String zip = czechpoint.getZip();
        String city = czechpoint.getCity();
        String street = czechpoint.getStreet();
        String type = czechpoint.getType();
        String name = czechpoint.getName();
        return computeMD5(type + "_" + zip + "_" + city + "_" + street + "_" + name);
    }

    private static String computeMD5(final String text) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(text.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hash = bigInt.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hash.length() < 32) {
            hash = "0" + hash;
        }
        return hash;
    }
}


