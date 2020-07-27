package ley.anvil.addonscript.wrapper;

import ley.anvil.addonscript.util.Utils;

import javax.annotation.Nonnull;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class FileOrLink {

    String link;

    public FileOrLink(String link) {
        this.link = link;
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
    public File getFile(@Nonnull String path) {
        if (isFile())
            return new File(Utils.slashEnd(path) + link.replace("file://", ""));
        throw new RuntimeException("This is no file");
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

}
