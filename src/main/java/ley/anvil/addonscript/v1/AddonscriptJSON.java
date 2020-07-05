package ley.anvil.addonscript.v1;

import ley.anvil.addonscript.curse.CurseforgeRepository;
import ley.anvil.addonscript.python.PythonInstaller;
import ley.anvil.addonscript.util.ASBase;
import ley.anvil.addonscript.installer.IInstaller;
import ley.anvil.addonscript.util.IRepository;
import ley.anvil.addonscript.installer.InternalDirInstaller;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddonscriptJSON extends ASBase {

    public Map<String, IInstaller> INSTALLERS;
    public Map<String, IRepository> REPOSITORIES;

    public static AddonscriptJSON fromJSON(String json) {
        return fromJSON(json, AddonscriptJSON.class);
    }

    public static AddonscriptJSON read(Reader reader) {
        return read(reader, AddonscriptJSON.class);
    }

    public static AddonscriptJSON create() {
        AddonscriptJSON as = new AddonscriptJSON();
        as.asversion = 1;
        return as;
    }

    public AddonscriptJSON() {
        INSTALLERS = new HashMap<>();
        REPOSITORIES = new HashMap<>();
    }

    public void setup() {
        INSTALLERS.put("internal.dir", new InternalDirInstaller());
        for (Installer i : installers) {
            INSTALLERS.put(i.id, i.getInstaller());
        }
        for (Repository r : repositories) {
            REPOSITORIES.put(r.id, r.getRepository());
        }
    }

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
     * External Addonscript files for this Addon, specific versions of specific editions
     */
    public List<External> external;

    /**
     * Links to external Addonscript files, that should be applied/merged to this
     */
    public List<String> apply;

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
         * Meta Information about this version
         */
        public VersionMeta meta;
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

    public static class VersionMeta {
        /**
         * The changelog of this version
         */
        public List<String> changelog;
        /**
         * The UNIX Timestamp when this version was released
         */
        public int timestamp;
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

        public IRepository getRepository() {
            switch (type) {
                case "curseforge": return new CurseforgeRepository();
                default: return null;
            }
        }

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

        public IInstaller getInstaller() {
            return new PythonInstaller(link);
        }
    }

    /**
     *
     */
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
         * Format: &lt;installerid&gt;:&lt;param 1&gt;:&lt;param 2&gt;...
         * Installer ID can be internal.&lt;some internal installer&gt;
         */
        public String installer = "internal.override";
        /**
         * A link or relative path to this file.
         * It can also be a path to a directory, if the installer supports directories
         */
        public Artifact file;
        /**
         * A list of options for this file.
         * Currently supported parameters:
         * "required" - This file is required for the addon
         * "optional" - This file is optional for the addon
         * "client" - This file works on the client side
         * "server" - This file works on the server side
         */
        public List<String> options;

        public boolean server() {
            return options != null && options.contains("server");
        }

        public boolean client() {
            return options != null && options.contains("client");
        }

    }

    public static class Relation {
        /**
         * An external Addonscript for this relation
         */
        public Script script;
        /**
         * The ID of the relation
         * This should be unique in this Addonscript file
         */
        public String id;
        /**
         * The installer for this file
         * Format: &lt;installerid&gt;:&lt;param 1&gt;:&lt;param 2&gt;...
         * Installer ID can be internal.&lt;some internal installer&gt;
         */
        public String installer = "internal.dir:mods";
        /**
         * The file of this relation
         * Can be a direct link or an artifact from a repository
         */
        public Artifact file;
        /**
         * The addon type of this relation
         * For example: mod, modloader, config, script...
         */
        public String type;
        /**
         * Meta information for this relation
         * This is not always useful, because some repositories, like curseforge, are
         * already exposing this information
         */
        public Meta meta;
        /**
         * A list of options for this relation.
         * Currently supportet parameters:
         * "required" - This relation is required for the addon
         * "optional" - This relation is optional for the addon
         * "client" - This relation works on the client side
         * "server" - This relation works on the server side
         * "included" - This relation is included in this addon (if this is a modpack)
         * "recommended" - This relation is recomended but not required for this addon
         * "incompatible" - This relation is incompatible with this addon
         */
        public List<String> options;

        public boolean server() {
            return options != null && options.contains("server");
        }

        public boolean client() {
            return options != null && options.contains("client");
        }

    }

    public static class External {

        public int versionid;
        public String edition;
        public String link;

    }

    public static class Script {

        public String link;
        public String version;

    }

    public static class Artifact {

        public String link;
        public String path;
        public String artifact;

    }

}
