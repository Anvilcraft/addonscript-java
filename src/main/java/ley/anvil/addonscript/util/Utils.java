package ley.anvil.addonscript.util;

import ley.anvil.addonscript.v1.AddonscriptJSON;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

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
        return s != null && !s.equals("");
    }

    public static String slashEnd(@Nonnull String s) {
        return s + (s.endsWith("/") ? "" : "/");
    }

}
