package ley.anvil.addonscript.wrapper;

import ley.anvil.addonscript.installer.DefaultInstaller;
import ley.anvil.addonscript.installer.DirInstaller;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface IInstaller {

    /**
     * This is the ID of this installer type.
     * @return the ID
     */
    String installerID();

    /**
     * This are the raw arguments of the installer.
     * @return An array of arguments. The positions are important.
     */
    String[] getArguments();

    @Nullable
    static IInstaller create(String id, List<String> arguments) {
        if (arguments == null || id.contains(":")) {
            String[] parts = id.split(":");
            id = parts[0];
            arguments = new ArrayList<>();
            arguments.addAll(Arrays.asList(parts).subList(1, parts.length));
        }
        switch (id) {
            case "internal.dir": return new DirInstaller(arguments);
            default: return new DefaultInstaller(id, arguments);
        }
    }

}
