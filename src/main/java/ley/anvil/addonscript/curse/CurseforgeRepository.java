package ley.anvil.addonscript.curse;

import com.therandomlabs.curseapi.CurseAPI;
import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.file.CurseFile;
import ley.anvil.addonscript.util.IRepository;

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

    public CurseFile getFile(String artifact) {
        String[] parts = artifact.split(":");
        if (parts.length >= 2) {
            int projectID = Integer.parseInt(parts[0]);
            int fileID = Integer.parseInt(parts[1]);
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
