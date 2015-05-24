package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Julien on 23.05.15.
 */
public class Config extends Recordable<HashMap<String, List<String>>> {

    public static final String PATH = "config.json";

    private static Config INSTANCE = null;

    private Config(String path) {
        super(path, new HashMap<String, List<String>>());
    }

    public static Config getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Config(PATH);
            INSTANCE.getData().put("gameofthrones", new ArrayList<String>());
        }
        return INSTANCE;
    }

    public static void setInstance(Config instance) {
        INSTANCE = instance;
    }

}
