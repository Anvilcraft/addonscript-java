package ley.anvil.addonscript.curse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ley.anvil.addonscript.wrapper.MetaData;

import java.util.List;

public class CurseMeta {

    static Gson gson = new GsonBuilder().create();

    public static CurseMeta fromJSON(String json) {
        return gson.fromJson(json, CurseMeta.class);
    }

    public MetaData toMeta() {
        MetaData meta = new MetaData();
        meta.website = websiteUrl;
        meta.name = name;
        for (Author a : authors) {
            meta.contributors.put(a.name, new String[]{"author"});
        }
        meta.description = new String[1];
        meta.description[0] = summary;
        for (Attachment a : attachments) {
            if (a.isDefault)
                meta.icon = a.url;
        }
        return meta;
    }

    public int id;
    public String name;
    public List<Author> authors;
    public List<Attachment> attachments;
    public String websiteUrl;
    public String summary;

    public static class Author {
        public String name;
    }

    public static class Attachment {
        public boolean isDefault;
        public String url;
    }

}
