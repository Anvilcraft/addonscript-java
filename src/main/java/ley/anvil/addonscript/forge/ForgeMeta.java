package ley.anvil.addonscript.forge;

import ley.anvil.addonscript.v1.AddonscriptJSON;

import java.util.ArrayList;

public class ForgeMeta extends AddonscriptJSON.Meta {

    public ForgeMeta() {
        name = "Minecraft Forge";
        contributors = new ArrayList<>();
        contributors.add(getContrib("LexManos", "owner")); //TODO Complete Contributor list
        website = "https://forums.minecraftforge.net/";
    }

    private AddonscriptJSON.Contributor getContrib(String name, String role) {
        AddonscriptJSON.Contributor con = new AddonscriptJSON.Contributor();
        con.name = name;
        con.roles = new ArrayList<>();
        con.roles.add(role);
        return con;
    }

}
