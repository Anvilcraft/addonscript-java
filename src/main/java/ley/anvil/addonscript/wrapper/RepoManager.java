package ley.anvil.addonscript.wrapper;

import ley.anvil.addonscript.util.HTTPRequest;
import ley.anvil.addonscript.util.IRepository;
import ley.anvil.addonscript.util.Utils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class RepoManager {

    HashMap<String, IRepository> repositories;

    public RepoManager() {
        repositories = new HashMap<>();
    }

    public void addRepository(@Nonnull String id, @Nonnull IRepository repo) {
        repositories.put(id, repo);
    }

    @HTTPRequest
    public String resolveArtifact(@Nonnull String artifact) {
        for (IRepository repo : repositories.values()) {
            String link = repo.getFileURL(artifact);
            if (Utils.notEmpty(link))
                return link;
        }
        throw new RuntimeException(artifact + " was not found in a known repository");
    }

    @HTTPRequest
    public Map<String, MetaData> getMeta(@Nonnull String[] artifacts) {
        Map<String, MetaData> metas = new HashMap<>();
        for (String a : artifacts) {
            for (IRepository r : repositories.values()) {
                MetaData data = r.getMeta(a);
                if (data != null)
                    metas.put(a, data);
            }
        }
        return metas;
    }

}
