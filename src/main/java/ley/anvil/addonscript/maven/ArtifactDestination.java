package ley.anvil.addonscript.maven;

import java.util.Arrays;
import java.util.List;

public class ArtifactDestination {

    public String group;
    public String id;
    public String version;
    public String[] others;

    public ArtifactDestination(String dest) {
        List<String> parts = Arrays.asList(dest.split(":"));
        if (parts.size() >= 3) {
            group = parts.get(0);
            id = parts.get(1);
            version = parts.get(2);
            others = parts.subList(3, parts.size()).toArray(new String[0]);
        } else {
            throw new RuntimeException("Invalid artifact: " + dest);
        }
    }

    public String getPOMPath() {
        StringBuilder sb = new StringBuilder();
        for (String s : group.split("\\.")) {
            sb.append(s).append("/");
        }
        sb.append(id).append("/").append(version).append("/");
        sb.append(id).append("-").append(version);
        sb.append(".pom");

        return sb.toString();
    }

    public String getPath(String filetype) {
        StringBuilder sb = new StringBuilder();
        for (String s : group.split("\\.")) {
            sb.append(s).append("/");
        }
        sb.append(id).append("/").append(version).append("/");
        sb.append(id).append("-").append(version);
        for (String s : others) {
            sb.append("-").append(s);
        }
        sb.append(".").append(filetype);

        return sb.toString();
    }

}
