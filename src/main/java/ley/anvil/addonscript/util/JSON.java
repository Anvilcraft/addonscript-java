package ley.anvil.addonscript.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class JSON {

    public static Gson gson = new GsonBuilder().create();
    
    public String toJSON() {
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return toJSON();
    }

    public String toFormattedJSON() {
        return formatJSON(toJSON());
    }


    public static String formatJSON(String json) {
        //TODO JSON formatting
        return json;
    }
    
}
