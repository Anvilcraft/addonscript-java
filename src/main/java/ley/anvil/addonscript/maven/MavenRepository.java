package ley.anvil.addonscript.maven;

import ley.anvil.addonscript.util.IRepository;
import ley.anvil.addonscript.v1.AddonscriptJSON;

public class MavenRepository implements IRepository {

    String url;

    public MavenRepository(String url) {
        this.url = url;
    }

    @Override
    public String getFileURL(String artifact) {
        ArtifactDestination dest = new ArtifactDestination(artifact);
        return dest.getPath(".jar");
    }

    @Override
    public AddonscriptJSON.Meta getMeta(String artifact) {
        return null;
    }
}
