package ley.anvil.addonscript.installer;

import ley.anvil.addonscript.wrapper.IInstaller;

import java.util.List;

public class DefaultInstaller implements IInstaller {

    String id;
    List<String> arguments;

    public DefaultInstaller(String id, List<String> arguments) {
        this.id = id;
        this.arguments = arguments;
    }

    @Override
    public String installerID() {
        return id;
    }

    @Override
    public String[] getArguments() {
        return arguments.toArray(new String[0]);
    }

}
