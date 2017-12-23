package com.wheezygold.happyserver.util;

import com.mashape.unirest.http.Unirest;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class UtilWeb {

    public static String getResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            InputStream in = connection.getInputStream();
            String encoding = connection.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            return IOUtils.toString(in, encoding);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static JSONObject getJSONResponse(String urlString) {
        Unirest.setTimeouts(2000, 2500);
        try {
            return Unirest.get(urlString).asJsonAsync().get().getBody().getObject();
        } catch (InterruptedException | ExecutionException e) {
            C.log("Error while fetching a JSON Response!");
            return null;
        }
    }

}
