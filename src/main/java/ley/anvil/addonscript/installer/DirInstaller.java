package ley.anvil.addonscript.installer;

import ley.anvil.addonscript.util.Utils;
import ley.anvil.addonscript.wrapper.IInstaller;

import javax.annotation.Nullable;
import java.io.File;
import java.util.List;

public class DirInstaller implements IInstaller {

    List<String> arguments;

    public DirInstaller(List<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String installerID() {
        return "internal.dir";
    }

    @Override
    public String[] getArguments() {
        return arguments.toArray(new String[0]);
    }

    public String getDestination() {
        return arguments.get(0);
    }

    public File getDestination(File mcdir) {
        return new File(Utils.slashEnd(mcdir.getPath()) + getDestination());
    }

    public boolean shouldRename() {
        return arguments.size() == 2;
    }

    @Nullable
    public String filename() {
        if (shouldRename()) {
            return arguments.get(1);
        } else {
            return null;
        }
    }

}
