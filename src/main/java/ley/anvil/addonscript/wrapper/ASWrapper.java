package ley.anvil.addonscript.wrapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import jdk.nashorn.api.scripting.URLReader;
import ley.anvil.addonscript.curse.CurseMeta;
import ley.anvil.addonscript.forge.ForgeMeta;
import ley.anvil.addonscript.installer.IInstaller;
import ley.anvil.addonscript.installer.InternalDirInstaller;
import ley.anvil.addonscript.util.Utils;
import ley.anvil.addonscript.v1.AddonscriptJSON;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.*;

public class ASWrapper {

    AddonscriptJSON json;
    Map<String, String> REPOSITORIES;
    public Map<String, IInstaller> INSTALLERS;
    public Map<String, AddonscriptJSON> ADDONS;
    public Map<Integer, ASWrapper.VersionWrapper> VERSIONS;

    public ASWrapper(AddonscriptJSON json) {
        this.json = json;
        REPOSITORIES = new HashMap<>();
        INSTALLERS = new HashMap<>();
        ADDONS = new HashMap<>();
        VERSIONS = new HashMap<>();
        INSTALLERS.put("internal.dir", new InternalDirInstaller());
        if (json.repositories != null) {
            for (AddonscriptJSON.Repository r : json.repositories) {
                REPOSITORIES.put(r.id, r.url);
            }
        }
        if (json.index != null) {
            for (AddonscriptJSON.IndexEntry e : json.index) {
                if (e.type != null && e.type.equals("addon"))
                    ADDONS.put(e.id, Utils.getFromURL(e.link));
                //TODO external versions
            }
        }
        if (json.versions != null) {
            for (AddonscriptJSON.Version v : json.versions) {
                if (!VERSIONS.containsKey(v.versionid))
                    VERSIONS.put(v.versionid, new VersionWrapper(v));
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
        if (VERSIONS.containsKey(versionid))
            return VERSIONS.get(versionid);
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

        @Nullable
        public AddonscriptJSON.Version getVersion() {
            return version;
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

        public FileOrLink get() {
            return new FileOrLink(getLink());
        }

        public String getLink() {
            if (!Utils.notEmpty(link) && Utils.notEmpty(file.artifact)) {
                String l = getArtifact().getPath();
                if (Utils.notEmpty(l))
                    link = l;
            }
            if (Utils.notEmpty(link))
                return link;
            throw new RuntimeException("JSON File broken");
        }

        public boolean isArtifact() {
            return Utils.notEmpty(file.artifact) && Utils.notEmpty(file.repository);
        }

        public ArtifactDestination getArtifact() {
            if (isArtifact()) {
                if (REPOSITORIES.containsKey(file.repository)) {
                    if (Utils.notEmpty(file.packaging))
                        return new ArtifactDestination(file.artifact, REPOSITORIES.get(file.repository), file.packaging);
                    else
                        return new ArtifactDestination(file.artifact, REPOSITORIES.get(file.repository));
                }
                else
                    throw new RuntimeException("Repository " + file.repository + " not existing");
            }
            throw new RuntimeException("This has no artifact");
        }

        @Deprecated
        public String getInstaller() {
            return file.installer;
        }

        public AddonscriptJSON.File getFile() {
            return file;
        }

        public List<String> getOptions() {
            if (file.options != null)
                return file.options;
            else
                return defaultOptions();
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
            return ADDONS.containsKey(relation.id);
        }

        public boolean isModloader() {
            if (Utils.notEmpty(relation.id) && relation.id.equals("forge")) {
                return true;
            }
            return Utils.notEmpty(relation.loaderfile);
        }

        public boolean hasLocalMeta() {
            if (Utils.notEmpty(relation.id) && relation.id.equals("forge"))
                return true;
            return relation.meta != null;
        }

        public MetaData getLocalMeta() {
            if (Utils.notEmpty(relation.id) && relation.id.equals("forge"))
                return new ForgeMeta();
            if (hasLocalMeta())
                return new MetaData(relation.meta);
            return new MetaData();
        }

        public Versions getVersions() {
            try {
                return new Versions(relation.versions);
            } catch (InvalidVersionSpecificationException e) {
                throw new RuntimeException("Error in JSON");
            }
        }

        public AddonscriptJSON.Relation getRelation() {
            return relation;
        }

        public List<String> getOptions() {
            if (relation.options != null)
                return relation.options;
            else
                return defaultOptions();
        }

    }

    public static Map<ArtifactDestination, MetaData> getMetaData(ArtifactDestination[] artifacts) {
        Map<ArtifactDestination, MetaData> meta = new HashMap<>();
        List<Integer> curseRequest = new ArrayList<>();
        for (ArtifactDestination dest : artifacts) {
            if (dest.isCurseforge())
                curseRequest.add(dest.getProjectID());
        }
        if (curseRequest.size() >= 1) {
            Gson gson = new GsonBuilder().create();
            String request = gson.toJson(curseRequest);
            String response = Utils.httpJSONPost("https://addons-ecs.forgesvc.net/api/v2/addon", request, new HashMap<>());
            JsonArray arr = gson.fromJson(response, JsonArray.class);
            for (JsonElement e : arr) {
                CurseMeta m = gson.fromJson(e, CurseMeta.class);
                for (ArtifactDestination a : artifacts) {
                    if (a.id.equals(String.valueOf(m.id))) {
                        meta.put(a, m.toMeta());
                        break;
                    }
                }
            }

        }
        return meta;
    }

}
