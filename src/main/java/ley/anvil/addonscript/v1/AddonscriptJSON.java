package ley.anvil.addonscript.v1;

import ley.anvil.addonscript.util.JSON;

import java.util.List;

public class AddonscriptJSON extends JSON {

    public static AddonscriptJSON fromJSON(String json) {
        return fromJSON(json, AddonscriptJSON.class);
    }

    public static AddonscriptJSON create() {
        AddonscriptJSON as = new AddonscriptJSON();
        as.asversion = 1;
        return as;
    }

    /**
     * The version of the AddonScript file
     */
    public int asversion;

    /**
     * The ID of the addon
     */
    public String id;

    /**
     * The type of the addon
     * For example mod or modpack
     */
    public String type;

    /**
     * A link to an external AddonScript file which should be loaded instead of this
     */
    public String extScript;

    /**
     * A list of versions of this addon
     */
    public List<Version> versions;

    /**
     * A list of repositories this file uses
     */
    public List<Repository> repositories;

    /**
     * A list of external installers this file uses
     */
    public List<Installer> installers;

    /**
     * Meta information for this addon
     */
    public Meta meta;

    public static class Version implements Comparable<Version> {
        /**
         * The name of this version
         * (for example: 1.0, 1.1, 2.0)
         */
        public String versionname;
        /**
         * The numeric ID of this version
         * Used to identify, which version is higher/lower than another
         * Can be -1 if this file uses only one version
         */
        public int versionid;
        /**
         * The Minecraft versions, which are compatile with this version
         */
        public List<String> mcversion;
        /**
         * The changelog of this version
         */
        public List<String> changelog;
        /**
         * The UNIX Timestamp when this version was released
         */
        public int timestamp;
        /**
         * A link to another Addonscript JSON file, which handles this version
         */
        public String extScript;
        /**
         * A list of files of this version
         */
        public List<File> files;
        /**
         * A lis of addons, that are related to this addon
         */
        public List<Relation> relations;

        @Override
        public int compareTo(Version o) {
            return versionid - o.versionid;
        }
    }

    public static class Contributor {
        /**
         * The name of the contributor
         */
        public String name;
        /**
         * The roles of the contributor
         * (for example: author, developer, owner ...)
         */
        public List<String> roles;
    }

    public static class Meta {
        /**
         * The name of the addon
         */
        public String name;

        /**
         * A link to the icon of this addon
         */
        public String icon;

        /**
         * A list of the contributors of this addon
         */
        public List<Contributor> contributors;

        /**
         * The website of the addon
         */
        public String website;

        /**
         * The description of the addon
         */
        public List<String> description;

        /**
         * A link to an external description
         */
        public String extdescr;

    }

    public static class Repository {
        /**
         * The ID of this repository
         * Must be unique to this file
         */
        public String id;
        /**
         * The type of this repository
         * Currently supported: curseforge, forge
         */
        public String type;
        /**
         * The base url of this repository
         */
        public String url;
    }

    public static class Installer {
        /**
         * The ID of this installer
         * Must be unique to this file
         */
        public String id;
        /**
         * A link or relative path to a python file, which is an installer
         */
        public String link;

    }

    public static class File {
        /**
         * The ID of this file.
         * If multiple files have the same ID,
         * Addonscript will interpret this files as identical.
         * Addonscript will then try to install the first of them
         * and only if this fails, it will try the next.
         */
        public String id;
        /**
         * The installer for this file
         * Format: <installerid>:<param 1>:<param 2>...
         * Installer ID can be internal.<some internal installer>
         */
        public String installer = "internal.override";
        /**
         * A link or relative path to this file.
         * It can also be a path to a directory, if the installer supports directories
         */
        public String file;
        /**
         * Should this file be on the client?
         */
        public boolean client = true;
        /**
         * Should this file be on the server?
         */
        public boolean server = true;
        /**
         * Is this file required or optional?
         */
        public boolean required = true;

    }

    public static class Relation {
        /**
         * The installer for this file
         * Format: <installerid>:<param 1>:<param 2>...
         * Installer ID can be internal.<some internal installer>
         */
        public String installer = "internal.dir:mods";
        /**
         * A link to the file, a link to another Addonscript JSON file or an artifact String
         * Artifact String format: <repository id>:<repository specific string>
         */
        public String file;
        /**
         * The type of this relation
         * Supported type: included, required, recommended, optional or incompatible
         */
        public String type = "included";
        /**
         * Meta information for this relation
         * This is not always useful, because some repositories, like curseforge, are
         * already exposing this information
         */
        public Meta meta;

        /**
         * Should this file be on the client?
         */
        public boolean client = true;
        /**
         * Should this file be on the server?
         */
        public boolean server = true;
        /**
         * Is this file required or optional?
         */
        public boolean required = true;

    }

}
