package ley.anvil.addonscript.forge;

import ley.anvil.addonscript.v1.AddonscriptJSON;

import java.util.ArrayList;

public class ForgeTools {

    public static void addForgeRepo(AddonscriptJSON as) {
        if (as.repositories == null) {
            as.repositories = new ArrayList<>();
        }
        boolean alreadyAdded = false;
        for (AddonscriptJSON.Repository repo : as.repositories) {
            if (repo.id != null && repo.id.equals("forge")) {
                alreadyAdded = true;
            }
        }
        if (!alreadyAdded) {
            AddonscriptJSON.Repository repo = new AddonscriptJSON.Repository();
            repo.id = "forge";
            repo.type = "forge";
            repo.url = "https://files.minecraftforge.net/";
            as.repositories.add(repo);
        }
    }

    public static void addForge(AddonscriptJSON as, int version, String forgeVersion) {
        addForgeRepo(as);
        if (as.versions == null) {
            as.versions = new ArrayList<>();
            as.versions.add(new AddonscriptJSON.Version());
        }
        AddonscriptJSON.Version ver = null;
        for (AddonscriptJSON.Version v : as.versions) {
            if (v.versionid == version || v.versionid == -1) {
                ver = v;
            }
        }
        if (ver != null) {
            AddonscriptJSON.Relation rel = new AddonscriptJSON.Relation();
            rel.type = "required";
            if (as.type != null && as.type.equals("modpack")) {
                rel.type = "included";
            }
            rel.installer = "internal.forge";
            rel.file = forgeVersion;
            if (ver.relations == null) {
                ver.relations = new ArrayList<>();
            }
            ver.relations.add(rel);
        }
    }



}
