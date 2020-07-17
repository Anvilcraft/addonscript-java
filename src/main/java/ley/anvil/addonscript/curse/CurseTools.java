package ley.anvil.addonscript.curse;

import com.therandomlabs.curseapi.CurseAPI;
import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.project.CurseProject;
import ley.anvil.addonscript.v1.AddonscriptJSON;

import java.util.ArrayList;
import java.util.Optional;

public class CurseTools {

    public static void addCurseRepo(AddonscriptJSON as) {
        if (as.repositories == null) {
            as.repositories = new ArrayList<>();
        }
        boolean alreadyAdded = false;
        for (AddonscriptJSON.Repository repo : as.repositories) {
            if (repo.type != null && repo.type.equals("curseforge")) {
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

    public static AddonscriptJSON.File toArtifact(int projectID, int fileID) {
        AddonscriptJSON.File artifact = new AddonscriptJSON.File();
        artifact.artifact = "curse:" + projectID + ":" + fileID;
        return artifact;
    }

    public static boolean isCurseArtifact(String artifact, AddonscriptJSON as) {
        String[] parts = artifact.split(":");
        if (parts.length == 3 && parts[0].equals("curse")) {
            try {
                int proj = Integer.parseInt(parts[1]);
                int file = Integer.parseInt(parts[0]);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    public static String getID(int projectID) {
        try {
            Optional<CurseProject> project = CurseAPI.project(projectID);
            if (project.isPresent()) {
                CurseProject proj = project.get();
                return proj.slug();
            }
        } catch (CurseException e) {
            return "NOID";
        }
        return "NOID";
    }

}
