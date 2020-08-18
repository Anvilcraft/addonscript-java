package ley.anvil.addonscript.wrapper;

import ley.anvil.addonscript.util.Utils;

import javax.annotation.Nonnull;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class FileOrLink {

    String link;
    File asdir;
    public IInstaller installer;


    public FileOrLink(String link) {
        this.link = link;
    }

    public FileOrLink(String link, IInstaller installer) {
        this.link = link;
        this.installer = installer;
    }

    public boolean isFile() {
        return Utils.notEmpty(link) && link.startsWith("file://");
    }

    public boolean isURL() {
        return Utils.notEmpty(link) && (link.startsWith("http://") || link.startsWith("https://"));
    }

    public String getLink() {
        return link;
    }

    /**
     * In Addonscript file:// links are relative paths from the Addonscript.json,
     * so you have to specify the path of the directory in which it is placed.
     * @param path The path to the directory, in which the Addonscript.json is placed
     * @return A File
     */
    @Deprecated
    public File getFile(@Nonnull String path) {
        if (isFile())
            return new File(Utils.slashEnd(path) + link.replace("file://", ""));
        throw new RuntimeException("This is no file");
    }

    /**
     * To call this, the ASDir should be set. If it isn't set, set it with setASDir before calling this.
     * @return The File, which is represented by this object
     */
    public File getFile() {
        if (isFile() && isASDirSet())
            return new File(Utils.slashEnd(asdir.getPath()) + link.replace("file://", ""));
        throw new RuntimeException("This is no file or the AS Dir was not set");
    }

    public URL getURL() {
        if (isURL()) {
            try {
                return new URL(link);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("This is no URL");
    }

    /**
     * In Addonscript file:// links are relative paths from the Addonscript.json,
     * so you have to specify the path of the directory in which it is placed.
     * @param dir The path to the directory, in which the Addonscript.json is placed
     * @return This FileOrLink object
     */
    public FileOrLink setASDir(File dir) {
        asdir = dir;
        return this;
    }

    public boolean isASDirSet() {
        return asdir != null;
    }

    public IInstaller getInstaller() {
        return installer;
    }

}
