package ley.anvil.addonscript.curse;

import com.google.gson.annotations.Expose;
import ley.anvil.addonscript.util.JSON;
import ley.anvil.addonscript.v1.AddonscriptJSON;

import java.util.ArrayList;
import java.util.List;

public class ManifestJSON extends JSON {

    public ManifestJSON fromJSON(String json) {
        return gson.fromJson(json, ManifestJSON.class);
    }

    @Expose
    public Minecraft minecraft;
    @Expose
    public String manifestType;
    @Expose
    public int manifestVersion;
    @Expose
    public String name;
    @Expose
    public String version;
    @Expose
    public String author;
    @Expose
    public List<File> files;
    @Expose
    public String overrides = "overrides";

    public static class Minecraft {
        @Expose
        public String version;
        @Expose
        public List<Modloader> modLoaders;
    }

    public static class Modloader {
        @Expose
        public String id;
        @Expose
        public boolean primary;

        public AddonscriptJSON.Relation toRelation(String mcv) {
            if (id != null && id.startsWith("forge-")) {
                AddonscriptJSON.Relation rel = new AddonscriptJSON.Relation();
                rel.type = "modloader";
                rel.id = "forge";
                rel.versions = "[" + id.replaceAll("forge", mcv) + "]";
                rel.options = new ArrayList<>();
                rel.options.add("required");
                rel.options.add("client");
                rel.options.add("server");
                rel.options.add("included");
                return rel;
            }
            return null;
        }

    }

    public static class File {
        @Expose
        public int projectID;
        @Expose
        public int fileID;
        @Expose
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
        as.id = name.toLowerCase();
        as.meta = new AddonscriptJSON.Meta();
        as.meta.contributors = new ArrayList<>();
        as.versions = new ArrayList<>();
        as.meta.contributors.add(getAuthor());
        as.meta.name = name;
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
