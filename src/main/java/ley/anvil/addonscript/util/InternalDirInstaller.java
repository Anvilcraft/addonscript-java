package ley.anvil.addonscript.util;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class InternalDirInstaller implements IInstaller {

    public static String ID = "internal.dir";

    @Override
    public void install(String[] params, String filelink) {
        if (params.length >= 1) {
            try {
                InputStream in = new URL(filelink).openStream();
                Files.copy(in, Paths.get(params[0]), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("No directory specified for file " + filelink);
        }
    }
}
