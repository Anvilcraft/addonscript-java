package ley.anvil.addonscript.forge;

import ley.anvil.addonscript.wrapper.MetaData;

public class ForgeMeta extends MetaData {

    public ForgeMeta() {
        name = "Minecraft Forge";
        contributors.put("LexManos", new String[]{"owner"}); //TODO Complete Contributor list
        website = "https://forums.minecraftforge.net/";
    }

}
