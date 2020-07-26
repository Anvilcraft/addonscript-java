package ley.anvil.addonscript.curse;

import ley.anvil.addonscript.util.JSON;
import ley.anvil.addonscript.v1.AddonscriptJSON;

import java.util.ArrayList;
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

        public AddonscriptJSON.Relation toRelation(String mcv) {
            if (id != null && id.startsWith("forge-")) {
                AddonscriptJSON.Relation rel = new AddonscriptJSON.Relation();
                rel.type = "modloader";
                rel.id = "forge";
                rel.versions = "[" + mcv +  id.replaceAll("forge", "") + "]";
                rel.options = new ArrayList<>();
                rel.options.add("required");
                rel.options.add("client");
                rel.options.add("required");
                rel.options.add("included");
                return rel;
            }
            return null;
        }

    }

    public static class File {
        public int projectID;
        public int fileID;
        public boolean required;

        public AddonscriptJSON.Relation toRelation() {
            AddonscriptJSON.Relation rel = new AddonscriptJSON.Relation();
            rel.file = new AddonscriptJSON.File();
            rel.file.repository = "curse";
            rel.file.artifact = "curse.maven:" + projectID + ":" + fileID;
            rel.file.installer = "internal.dir:mods";
            rel.options = new ArrayList<>();
            rel.options.add(required ? "required" : "optional");
            rel.options.add("client");
            rel.options.add("server");
            rel.options.add("included");
            rel.id = "" + projectID;
            rel.type = "mod";
            return rel;
        }

    }

    AddonscriptJSON.Contributor getAuthor() {
        AddonscriptJSON.Contributor author = new AddonscriptJSON.Contributor();
        author.name = this.author;
        author.roles = new ArrayList<>();
        author.roles.add("author");
        return author;
    }

    AddonscriptJSON.Version getVersion() {
        AddonscriptJSON.Version version = new AddonscriptJSON.Version();
        version.versionid = -1;
        version.version = this.version;
        version.mcversion = new ArrayList<>();
        version.mcversion.add(minecraft.version);
        version.files = new ArrayList<>();
        version.relations = new ArrayList<>();

        AddonscriptJSON.File overrides = new AddonscriptJSON.File();
        overrides.id = "overrides";
        overrides.installer = "internal.override";
        overrides.link = "file://" + this.overrides;
        version.files.add(overrides);

        for (File f : files) {
            version.relations.add(f.toRelation());
        }

        if (minecraft != null) {
            for (Modloader l : minecraft.modLoaders) {
                version.relations.add(l.toRelation(minecraft.version));
            }
        }
        
        return version;
    }

    public AddonscriptJSON toAS() {
        AddonscriptJSON as = AddonscriptJSON.create();
        as.type = "modpack";
        as.id = name;
        as.meta = new AddonscriptJSON.Meta();
        as.meta.contributors = new ArrayList<>();
        as.versions = new ArrayList<>();
        as.meta.contributors.add(getAuthor());
        as.versions.add(getVersion());

        as.repositories = new ArrayList<>();
        AddonscriptJSON.Repository curseRepo = new AddonscriptJSON.Repository();
        curseRepo.id = "curse";
        curseRepo.type = "curseforge";
        curseRepo.url = "https://www.cursemaven.com/";
        as.repositories.add(curseRepo);

        return as;
    }

}
