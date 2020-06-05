package ley.anvil.addonscript.util;

import ley.anvil.addonscript.v1.AddonscriptJSON;

import java.io.Reader;

public abstract class ASBase extends JSON {

    public ASBase fromJSON(Reader reader) {
        ASBase base = gson.fromJson(reader, ASBase.class);
        return gson.fromJson(reader, base.getImpl());
    }

    /**
     * The version of the AddonScript file
     */
    public int asversion;

    public Class<? extends ASBase> getImpl() {
        switch (asversion) {
            case 1: return AddonscriptJSON.class;
            default: return ASBase.class;
        }
    }

}
