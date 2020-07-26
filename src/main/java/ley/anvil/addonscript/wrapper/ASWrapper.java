package ley.anvil.addonscript.wrapper;

import jdk.nashorn.api.scripting.URLReader;
import ley.anvil.addonscript.installer.InternalDirInstaller;
import ley.anvil.addonscript.util.HTTPRequest;
import ley.anvil.addonscript.util.Utils;
import ley.anvil.addonscript.v1.AddonscriptJSON;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ASWrapper {

    AddonscriptJSON json;
    Indexes indexes;
    RepoManager repoManager;

    public ASWrapper(AddonscriptJSON json) {
        this.json = json;
        indexes = new Indexes();
        repoManager = new RepoManager();
        indexes.INSTALLERS.put("internal.dir", new InternalDirInstaller());
        if (json.repositories != null) {
            for (AddonscriptJSON.Repository r : json.repositories) {
                repoManager.addRepository(r.id, r.getRepository());
            }
        }
        if (json.index != null) {
            for (AddonscriptJSON.IndexEntry e : json.index) {
                if (e.type != null && e.type.equals("addon"))
                    indexes.ADDONS.put(e.id, Utils.getFromURL(e.link));
                //TODO external versions
            }
        }
        if (json.versions != null) {
            for (AddonscriptJSON.Version v : json.versions) {
                if (!indexes.VERSIONS.containsKey(v.versionid))
                    indexes.VERSIONS.put(v.versionid, new VersionWrapper(v));
            }
        }
    }

    public ASWrapper(File file) throws FileNotFoundException {
        this(AddonscriptJSON.read(new FileReader(file)));
    }

    public ASWrapper(URL url) {
        this(AddonscriptJSON.read(new URLReader(url)));
    }

    public AddonscriptJSON getJson() {
        return json;
    }

    @Override
    public String toString() {
        return json.toJSON();
    }

    public RepoManager getRepositories() {
        return repoManager;
    }

    //Options

    public List<String> defaultOptions() {
        List<String> list = new ArrayList<>();
        list.add("client");
        list.add("server");
        list.add("required");
        return list;
    }

    //Versions

    public VersionWrapper getDefaultVersion() {
        for (AddonscriptJSON.Version v : json.versions) {
            if (v.versionid == -1)
                return new VersionWrapper(v);
        }
        if (json.versions.size() > 0)
            return new VersionWrapper(json.versions.get(0));
        return new VersionWrapper();
    }

    public VersionWrapper getVersion(int versionid) {
        if (indexes.VERSIONS.containsKey(versionid))
            return indexes.VERSIONS.get(versionid);
        return new VersionWrapper();
    }

    public VersionWrapper getVersion(String versionCondition) {
        return null; //TODO Interpret version range
    }


    public class VersionWrapper {

        AddonscriptJSON.Version version;

        public VersionWrapper(AddonscriptJSON.Version version) {
            this.version = version;
        }

        public VersionWrapper() {
            this.version = null;
        }

        public boolean exists() {
            return version != null;
        }

        public List<RelationWrapper> getRelations(String[] conditions, @Nullable String type) {
            List<RelationWrapper> list = new ArrayList<>();
            if (exists() && version.relations != null) {
                for (AddonscriptJSON.Relation r : version.relations) {
                    List<String> opt;
                    if (r.options != null)
                        opt = r.options;
                    else
                        opt = defaultOptions();
                    if (opt.containsAll(Arrays.asList(conditions)) && (type== null || r.type.equals(type))) {
                        list.add(new RelationWrapper(r));
                    }
                }
            }
            return list;
        }

        public List<FileWrapper> getFiles(String[] conditions, @Nullable String installer) {
            List<FileWrapper> list = new ArrayList<>();
            if (exists() && version.files != null) {
                for (AddonscriptJSON.File f : version.files) {
                    List<String> opt;
                    if (f.options != null)
                        opt = f.options;
                    else
                        opt = defaultOptions();
                    if (opt.containsAll(Arrays.asList(conditions)) && (installer == null || f.installer.startsWith(installer))) {
                        list.add(new FileWrapper(f));
                    }
                }
            }
            return list;
        }

        public String getVersionName() {
            if (exists() && Utils.notEmpty(version.version))
                return version.version;
            return "";
        }

    }

    public class FileWrapper {

        AddonscriptJSON.File file;
        String link;

        public FileWrapper(AddonscriptJSON.File file) {
            this.file = file;
            if (Utils.notEmpty(file.link)) {
                link = file.link;
            }
        }

        public boolean isURL() {
            if (link != null && (link.startsWith("http://") || link.startsWith("https://"))) {
                return true;
            }
            if (Utils.notEmpty(file.artifact)) {
                return true;
            }
            return false;
        }

        public boolean isFile() {
            return Utils.notEmpty(link) && link.startsWith("file://");
        }

        public File asFile(String jsonDir) {
            if (isFile()) {
                return new File(Utils.slashEnd(jsonDir) + link.replace("file://", ""));
            }
            throw new RuntimeException("This is not a local file, try to check it before calling it.");
        }

        @HTTPRequest
        public String getLink() {
            if (!Utils.notEmpty(link) && Utils.notEmpty(file.artifact)) {
                String l = repoManager.resolveArtifact(file.artifact);
                if (Utils.notEmpty(l))
                    link = l;
            }
            if (Utils.notEmpty(link))
                return link;
            throw new RuntimeException("JSON File broken");
        }

        public boolean isArtifact() {
            return Utils.notEmpty(file.artifact);
        }

        public String getArtifact() {
            if (isArtifact())
                return file.artifact;
            return "";
        }

        @Deprecated
        public String getInstaller() {
            return file.installer;
        }

    }

    public class RelationWrapper {

        AddonscriptJSON.Relation relation;

        public RelationWrapper(AddonscriptJSON.Relation relation) {
            this.relation = relation;
        }

        public boolean hasFile() {
            return relation != null && relation.file != null;
        }

        public FileWrapper getFile() {
            if (hasFile())
                return new FileWrapper(relation.file);
            throw new RuntimeException("Relation has no file");
        }

        public boolean isInIndex() {
            return indexes.ADDONS.containsKey(relation.id);
        }

        public boolean isModloader() {
            if (Utils.notEmpty(relation.id) && relation.id.equals("forge")) {
                return true;
            }
            return Utils.notEmpty(relation.loaderfile);
        }

        public boolean hasLocalMeta() {
            return relation.meta != null;
        }

        public MetaData getLocalMeta() {
            if (hasLocalMeta())
                return new MetaData(relation.meta);
            return new MetaData();
        }




    }

}
