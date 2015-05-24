package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import sun.org.mozilla.javascript.internal.ScriptRuntime;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Julien on 23.05.15.
 */
public class Config {

    public static final String path = "config.json";
    private static Config instance = null;

    private HashMap<String, ArrayList<String>> configuration = null;

    private static Config getInstance() {
        if(instance == null) {
            try {
                instance = load(path);
            }catch (Exception e) {
                instance = new Config(path);
            }
        }
        return instance;
    }

    private static Config load(String path) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = new BufferedReader(new FileReader(path));
        instance = gson.fromJson(br, Config.class);
        br.close();
        return instance;
    }

    public Config() {
        this.configuration = new HashMap<String, ArrayList<String>>();
    }

    public void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        PrintWriter pw = new PrintWriter(path);
        pw.write(gson.toJson(instance));
        pw.close();
    }
}
