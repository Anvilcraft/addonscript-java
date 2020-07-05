package ley.anvil.addonscript.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.io.Writer;

public abstract class JSON {

    public static Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    
    public String toJSON() {
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return toJSON();
    }

    public void write(Writer writer) {
        gson.toJson(this, writer);
    }

    public static <T> T read(Reader reader, Class<T> type) {
        return gson.fromJson(reader, type);
    }

    public static <T> T fromJSON(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }
    
}
