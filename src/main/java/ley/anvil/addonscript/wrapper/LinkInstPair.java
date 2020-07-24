package ley.anvil.addonscript.wrapper;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class LinkInstPair {

    public String link;
    public String installer;

    public LinkInstPair(String link, String installer) {
        this.link = link;
        this.installer = installer;
    }

    public String getLink() {
        return link;
    }

    public String getInstaller() {
        return installer;
    }

    public boolean isURL() {
        return link.startsWith("http://") || link.startsWith("https://");
    }

    public boolean isFile() {
        return link.startsWith("file://");
    }

    public File asFile(String jsonDir) {
        if (isFile()) {
            String f = link.replace("file://", jsonDir + (jsonDir.endsWith("/") ? "" : "/"));
            return new File(f);
        }
        throw new RuntimeException("This is not a file");
    }

    public URL asURL() {
        if (isURL()) {
            try {
                return new URL(link);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("This is not a URL");
    }

}
