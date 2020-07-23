package ley.anvil.addonscript.util;

import ley.anvil.addonscript.installer.IInstaller;
import ley.anvil.addonscript.v1.AddonscriptJSON;

import java.util.HashMap;
import java.util.Map;

public class Indexes {

    public Indexes() {
        INSTALLERS = new HashMap<>();
        REPOSITORIES = new HashMap<>();
        ADDONS = new HashMap<>();
        VERSIONS = new HashMap<>();
    }

    public Map<String, IInstaller> INSTALLERS;
    public Map<String, IRepository> REPOSITORIES;
    public Map<String, AddonscriptJSON> ADDONS;
    public Map<Integer, AddonscriptJSON.Version> VERSIONS;

}
