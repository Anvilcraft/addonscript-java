package ley.anvil.addonscript.curse;

import ley.anvil.addonscript.v1.AddonscriptJSON;

import java.util.ArrayList;

public class CurseTools {

    public static void addCurseRepo(AddonscriptJSON as) {
        if (as.repositories == null) {
            as.repositories = new ArrayList<>();
        }
        boolean alreadyAdded = false;
        for (AddonscriptJSON.Repository repo : as.repositories) {
            if (repo.id != null && repo.id.equals("curse")) {
                alreadyAdded = true;
            }
        }
        if (!alreadyAdded) {
            AddonscriptJSON.Repository curseRepo = new AddonscriptJSON.Repository();
            curseRepo.id = "curse";
            curseRepo.type = "curseforge";
            curseRepo.url = "https://www.curseforge.com/minecraft/";
            as.repositories.add(curseRepo);
        }
    }

    public static String toArtifact(int projectID, int fileID) {
        return "curse>" + projectID + ":" + fileID;
    }

    public static boolean isCurseArtifact(String artifact, AddonscriptJSON as) {
        String[] parts = artifact.split(">");
        if (parts.length == 2 && as.repositories != null) {
            for (AddonscriptJSON.Repository repo : as.repositories) {
                if (repo.type != null && repo.type.equals("curseforge") && parts[0].equals(repo.id)) {
                    return true;
                }
            }
        }
        return false;
    }

}
