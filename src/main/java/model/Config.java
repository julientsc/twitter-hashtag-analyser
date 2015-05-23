package model;

/**
 * Created by Julien on 23.05.15.
 */
public class Config {

    private static Config instance = null;
    public static Config getInstance() {
        if(instance == null) {
            instance = new Config();
        }
        return instance;
    }
}
