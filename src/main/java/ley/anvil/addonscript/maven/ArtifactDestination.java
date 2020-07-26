package ley.anvil.addonscript.maven;

import ley.anvil.addonscript.util.Utils;

public class ArtifactDestination {

    public String repo;
    public String group;
    public String id;
    public String version;
    public String packaging;
    public String addition;

    public ArtifactDestination(String artifact, String repoLink) {
        this(artifact, repoLink, "jar");
    }

    public ArtifactDestination(String artifact, String repoLink, String packaging) {
        String[] parts = artifact.split(":");
        if (parts.length >= 3) {
            group = parts[0];
            id = parts[1];
            version = parts[2];
        }
        if (parts.length >= 4) {
            addition = parts[3];
        }
        repo = repoLink;
        this.packaging = packaging;
    }

    public String getPOMPath() {
        return getFile("pom", null);
    }

    public String getPath() {
        return getFile(packaging, addition);
    }

    private String getFile(String type, String addition) {
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.slashEnd(repo));
        for (String s : group.split("\\.")) {
            sb.append(s).append("/");
        }
        sb.append(id).append("/").append(version).append("/");
        sb.append(id).append("-").append(version);
        if (addition != null)
            sb.append("-").append(addition);
        sb.append(".").append(type);

        return sb.toString();
    }

    //Curseforge specific

    public boolean isCurseforge() {
        return repo.equals("https://www.cursemaven.com/") && group.equals("curse.maven");
    }

    public int getFileID() {
        return Integer.parseInt(version);
    }

    public int getProjectID() {
        return Integer.parseInt(id);
    }

}
