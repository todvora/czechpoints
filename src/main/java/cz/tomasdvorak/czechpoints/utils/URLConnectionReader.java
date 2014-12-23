package cz.tomasdvorak.czechpoints.utils;

import java.net.*;
import java.io.*;

public class URLConnectionReader {

    public static String getText(final String url) throws Exception {
        System.out.println("Downloading page: " + url);
        final URL website = new URL(url);
        final URLConnection connection = website.openConnection();
        final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        if (connection instanceof HttpURLConnection) {
            ((HttpURLConnection)connection).disconnect();
        }
        return response.toString();
    }
}
