package ley.anvil.addonscript.wrapper;

import ley.anvil.addonscript.util.Utils;
import ley.anvil.addonscript.v1.AddonscriptJSON;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class MetaData {

    public MetaData() {
        contributors = new HashMap<>();
    }

    public MetaData(AddonscriptJSON.Meta meta) {
        this();
        name = meta.name;
        icon = meta.icon;
        website = meta.website;
        if (meta.description != null)
            description = meta.description.toArray(new String[0]);
        if (meta.contributors != null) {
            for (AddonscriptJSON.Contributor c : meta.contributors) {
                String[] roles;
                if (c.roles != null) {
                    roles = c.roles.toArray(new String[0]);
                } else {
                    roles = new String[0];
                }
                if (Utils.notEmpty(c.name))
                    contributors.put(c.name, roles);
            }
        }
    }

    @Nullable
    public String name;
    @Nullable
    public String icon;
    @Nullable
    public String website;
    @Nullable
    public String[] description;
    @Nonnull
    public Map<String, String[]> contributors;


}
