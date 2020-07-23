package ley.anvil.addonscript.curse;

import com.therandomlabs.curseapi.CurseAPI;
import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.file.CurseFile;
import com.therandomlabs.curseapi.project.CurseProject;
import ley.anvil.addonscript.util.IRepository;
import ley.anvil.addonscript.v1.AddonscriptJSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class CurseforgeRepository implements IRepository {

    @Override
    public String getFileURL(String artifact) {
        CurseFile file = getFile(artifact);
        if (file != null) {
            return file.downloadURL().toString();
        }
        return "";
    }

    @Override
    public AddonscriptJSON.Meta getMeta(String artifact) {
        AddonscriptJSON.Meta meta = null;
        CurseFile file = getFile(artifact);
        if (file != null) {
            meta = new AddonscriptJSON.Meta();
            try {
                CurseProject project = file.project();
                meta.name = project.name();
                String desc = project.descriptionPlainText();
                meta.description = Arrays.asList(desc.split("\n"));
                meta.website = project.url().toString();
                meta.contributors = new ArrayList<>();
                AddonscriptJSON.Contributor owner = new AddonscriptJSON.Contributor();
                owner.roles = new ArrayList<>();
                owner.roles.add("owner");
                owner.name = project.author().name();
                meta.contributors.add(owner);
                meta.icon = project.logo().url().toString();
            } catch (CurseException e) {
                e.printStackTrace();
            }
        }
        return meta;
    }

    public CurseFile getFile(String artifact) {
        String[] parts = artifact.split(":");
        if (parts.length >= 3 && parts[0].equals("curse")) {
            int projectID = Integer.parseInt(parts[1]);
            int fileID = Integer.parseInt(parts[2]);
            try {
                Optional<CurseFile> file = CurseAPI.file(projectID, fileID);
                if (file.isPresent())
                    return file.get();
            } catch (CurseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
