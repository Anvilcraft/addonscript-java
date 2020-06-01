package ley.anvil.addonscript.util;

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

}
