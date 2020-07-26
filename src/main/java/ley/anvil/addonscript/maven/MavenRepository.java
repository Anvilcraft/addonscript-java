package ley.anvil.addonscript.maven;

import ley.anvil.addonscript.util.IRepository;
import ley.anvil.addonscript.wrapper.MetaData;

public class MavenRepository implements IRepository {

    //TODO Complete this
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
    public MetaData getMeta(String artifact) {
        return null;
    }
}
