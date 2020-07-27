package ley.anvil.addonscript.wrapper;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;

import javax.annotation.Nullable;

public class Versions {

    VersionRange range;
    String latest;

    public Versions(String range) throws InvalidVersionSpecificationException {
        this.range = VersionRange.createFromVersionSpec(range);
        String noBrack = range.replace("[", "").replace("]", "").replace("(", "").replace(")", "");
        String[] parts = noBrack.split(",");
        for (int i = parts.length - 1; i >= 0; i--) {
            if (this.range.containsVersion(new DefaultArtifactVersion(parts[i]))) {
                latest = parts[i];
                break;
            }
        }
    }

    public boolean contains(String version) {
        return range.containsVersion(new DefaultArtifactVersion(version));
    }

    @Nullable
    public String getLatestKnown() {
        return latest;
    }


}
