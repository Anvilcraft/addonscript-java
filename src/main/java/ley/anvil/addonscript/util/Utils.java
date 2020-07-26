package ley.anvil.addonscript.util;

import ley.anvil.addonscript.v1.AddonscriptJSON;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Utils {

    public static AddonscriptJSON getFromURL(String link) {
        try {
            return AddonscriptJSON.read(new InputStreamReader(new URL(link).openStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return new AddonscriptJSON();
        }
    }

    public static boolean notEmpty(@Nullable String s) {
        return s != null && !s.isEmpty();
    }

    public static String slashEnd(@Nonnull String s) {
        return s + (s.endsWith("/") ? "" : "/");
    }

    public static String httpJSONPost(String link, String payload, Map<String, String> headers) {
        try {
            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            for (String k : headers.keySet()) {
                con.setRequestProperty(k, headers.get(k));
            }
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = payload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
