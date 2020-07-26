package ley.anvil.addonscript.curse;

import com.therandomlabs.curseapi.CurseAPI;
import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.file.CurseFile;
import com.therandomlabs.curseapi.project.CurseProject;
import ley.anvil.addonscript.util.IRepository;
import ley.anvil.addonscript.wrapper.MetaData;

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
    public MetaData getMeta(String artifact) {
        MetaData meta = null;
        CurseFile file = getFile(artifact);
        if (file != null) {
            meta = new MetaData();
            try {
                CurseProject project = file.project();
                meta.name = project.name();
                String desc = project.descriptionPlainText();
                meta.description = desc.split("\n");
                meta.website = project.url().toString();
                meta.contributors.put(project.author().name(), new String[]{"owner"});
                meta.icon = project.logo().url().toString();
            } catch (CurseException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("WARNUNG: No file for Curseforge artifact " + artifact + " found!");
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
