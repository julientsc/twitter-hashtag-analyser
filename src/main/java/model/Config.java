package model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Julien on 23.05.15.
 */
public class Config extends Recordable<HashMap<String, ArrayList<String>>> {

    public static final String PATH = "config.json";
    public static final Class CLASS = new HashMap<String, ArrayList<String>>().getClass();

    private static Config INSTANCE = null;

    private Config(String path, Class c) {
        super(path, c);
    }

    public static Config getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Config(PATH, CLASS);
        }
        return INSTANCE;
    }

    public static void setInstance(Config instance) {
        INSTANCE = instance;
    }
}
