package ley.anvil.addonscript.python;

import ley.anvil.addonscript.installer.IInstaller;
import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class PythonInstaller implements IInstaller {

    String script;
    PythonInterpreter python;

    public PythonInstaller(String scriptlink) {
        script = getScript(scriptlink);
        python = new PythonInterpreter();
    }

    @Override
    public void install(String[] params, File input) {
        python.set("input", input.getAbsolutePath());
        python.set("params", params);
        python.exec(script);
    }

    public String getScript(String url) {
        try {
            URL scripturl = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(scripturl.openStream()));
            StringBuilder script = new StringBuilder();
            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                script.append(inputLine);
            reader.close();
            return script.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
