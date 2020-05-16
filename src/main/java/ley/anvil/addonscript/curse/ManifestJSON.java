package ley.anvil.addonscript.curse;

import ley.anvil.addonscript.util.JSON;
import ley.anvil.addonscript.v1.AddonscriptJSON;

import java.util.List;

public class ManifestJSON extends JSON {

    public ManifestJSON fromJSON(String json) {
        return gson.fromJson(json, ManifestJSON.class);
    }

    public Minecraft minecraft;

    public String manifestType;

    public String manifestVersion;

    public String name;

    public String version;

    public String author;

    public List<File> files;

    public String overrides = "overrides";

    public static class Minecraft {
        public String version;
        public List<Modloader> modLoaders;
    }

    public static class Modloader {
        public String id;
        public boolean primary;
    }

    public static class File {
        public int projectID;
        public int fileID;
        public boolean required;

        public AddonscriptJSON.Relation toRelation() {
            AddonscriptJSON.Relation rel = new AddonscriptJSON.Relation();
            rel.file = CurseTools.toArtifact(projectID, fileID);
            return rel;
        }

    }

}
