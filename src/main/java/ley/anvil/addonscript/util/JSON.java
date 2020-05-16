package ley.anvil.addonscript.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class JSON {

    public static Gson gson = new GsonBuilder().create();
    
    public String toJSON() {
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return toJSON();
    }
    
}
