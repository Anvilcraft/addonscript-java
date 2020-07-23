package ley.anvil.addonscript.installer;

import ley.anvil.addonscript.installer.IInstaller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class InternalDirInstaller implements IInstaller {

    @Override
    public void install(String[] params, File input) {
        if (params.length >= 1) {
            try {
                InputStream in = new FileInputStream(input);
                Files.copy(in, Paths.get(params[0]), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("No directory specified for file " + input.toString());
        }
    }

}
