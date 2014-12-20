package cz.tomasdvorak.czechpoints.utils;

import java.net.*;
import java.io.*;

public class URLConnectionReader {

    public static String getText(String url) throws Exception {
        System.out.println("Downloading page: " + url);
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
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
