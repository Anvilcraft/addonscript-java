package ley.anvil.addonscript.v1;

import com.google.gson.annotations.Expose;
import ley.anvil.addonscript.util.ASBase;
import ley.anvil.addonscript.util.Upcoming;

import java.io.Reader;
import java.util.List;

public class AddonscriptJSON extends ASBase {

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

    /**
     * The ID of the addon
     */
    @Expose
    public String id;

    /**
     * The type of the addon
     * For example mod or modpack
     */
    @Expose
    public String type;

    @Expose
    public List<IndexEntry> index;

    /**
     * A list of versions of this addon
     */
    @Expose
    public List<Version> versions;

    /**
     * A list of repositories this file uses
     */
    @Expose
    public List<Repository> repositories;

    /**
     * Optional
     * Meta information for this addon
     */
    @Expose
    public Meta meta;

    public static class Version implements Comparable<Version> {
        /**
         * The name of this version
         * (for example: 1.0, 1.1, 2.0)
         */
        @Expose
        public String version;
        /**
         * The numeric ID of this version
         * Used to identify, which version is higher/lower than another
         * Can be -1 if this file uses only one version
         */
        @Expose
        public int versionid;
        /**
         * The Minecraft versions, which are compatile with this version
         */
        @Expose
        public List<String> mcversion;
        /**
         * Optional
         * Meta Information about this version
         */
        @Expose
        public VersionMeta meta;
        /**
         * A list of files of this version
         */
        @Expose
        public List<File> files;
        /**
         * A lis of addons, that are related to this addon
         */
        @Expose
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
        @Expose
        public String name;
        /**
         * The roles of the contributor
         * (for example: author, developer, owner ...)
         */
        @Expose
        public List<String> roles;
    }

    public static class Meta {
        /**
         * The name of the addon
         */
        @Expose
        public String name;

        /**
         * A link to the icon of this addon
         */
        @Expose
        public String icon;

        /**
         * A list of the contributors of this addon
         */
        @Expose
        public List<Contributor> contributors;

        /**
         * The website of the addon
         */
        @Expose
        public String website;

        /**
         * The description of the addon
         */
        @Expose
        public List<String> description;

    }

    public static class VersionMeta {
        /**
         * The changelog of this version
         */
        @Expose
        public List<String> changelog;
        /**
         * The UNIX Timestamp when this version was released
         */
        @Expose
        public int timestamp;
    }

    public static class Repository {
        /**
         * The ID of this repository
         * Must be unique to this file
         */
        @Expose
        public String id;
        /**
         * The type of this repository
         * Currently supported: curseforge, forge
         */
        @Expose
        @Deprecated
        public String type;
        /**
         * The base url of this repository
         */
        @Expose
        public String url;

    }

    public static class File {
        /**
         * The ID of this file.
         * If multiple files have the same ID,
         * Addonscript will interpret this files as identical.
         * Addonscript will then try to install the first of them
         * and only if this fails, it will try the next.
         */
        @Expose
        public String id;
        /**
         * The installer for this file
         * Format: &lt;installerid&gt;:&lt;param 1&gt;:&lt;param 2&gt;...
         * Installer ID can be internal.&lt;some internal installer&gt;
         */
        @Expose
        public String installer = "internal.dir";
        /**
         * This are the arguments for the installer
         */
        @Upcoming
        @Expose
        public List<String> arguments;
        /**
         * Optional: Use this or artifact
         * A link to this file. Can also point to a relative file or directory by using file://&lt;relative path&gt;.
         */
        @Expose
        public String link;
        /**
         * Optional: Use this or link
         * An artifact from a repository, which is this file
         */
        @Expose
        public String artifact;
        @Expose
        public String repository;
        @Expose
        public String packaging;
        /**
         * Optional: Defaults if empty
         * (Defaults = required, client, server)
         * A list of options for this file.
         * Currently supported parameters:
         * "required" - This file is required for the addon
         * "optional" - This file is optional for the addon
         * "client" - This file works on the client side
         * "server" - This file works on the server side
         */
        @Expose
        public List<String> options;

    }

    public static class Relation {

        /**
         * The ID of the relation
         * This should be unique in this Addonscript file
         */
        @Expose
        public String id;

        @Deprecated
        @Expose
        public String loaderfile;

        /**
         * Optional: Wildcard if empty
         * A version range string, which specifies, which versions of the addon can be used
         */
        @Expose
        public String versions;
        /**
         * Optional: Use either this or script
         * The file of this relation
         * Can be a direct link or an artifact from a repository
         */
        @Expose
        public File file;
        /**
         * The addon type of this relation
         * For example: mod, modloader, config, script...
         */
        @Expose
        public String type;
        /**
         * Optional
         * Meta information for this relation
         * This is not always useful, because some repositories, like curseforge or external Addonscripts are
         * already exposing this information
         */
        @Expose
        public Meta meta;
        /**
         * Optional: Defaults if empty
         * (Defaults = required[included instead, if this is a modpack], client, server)
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
        @Expose
        public List<String> options;
    }

    public static class IndexEntry {
        @Expose
        public String type;
        @Expose
        public String link;
        @Expose
        public int versionid;
        @Expose
        public String edition;
        @Expose
        public String id;
    }

    public static class Edition {
        /**
         * The id of the edition
         */
        public String id;
        /**
         * Optional: Wildcard if empty
         * A version range string, which specifies, which versions have this edition
         */
        public String versions;
        /**
         * Optional
         * Meta information about this edition
         */
        public Meta meta;
    }

}
