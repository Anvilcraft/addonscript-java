package ley.anvil.addonscript.util;

import ley.anvil.addonscript.v1.AddonscriptJSON;

/**
 * Interface for all repository types
 */
public interface IRepository {

    /**
     * Gets the file URL from an artifact
     * @param artifact The artifact without the repo ID prefix
     * @return The URL of the file
     */
    String getFileURL(String artifact);

    /**
     * Gets meta information about an artifact.
     * Empty Object if nothing was found.
     * @param artifact The artifact without the repo ID prefix
     * @return A Meta object with meta information
     */
    AddonscriptJSON.Meta getMeta(String artifact);

}
