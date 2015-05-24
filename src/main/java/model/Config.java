package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Julien on 23.05.15.
 */
public class Config {

    public static final String path = "config.json";

    private static Config instance = null;

    private HashMap<String, ArrayList<String>> data = null;

    private Config() {
        this.data = new HashMap<>();
    }

    public static Config getInstance() {
        if (instance == null) {
            try {
                instance = load(path);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                instance = new Config();
            }
        }
        return instance;
    }

    private static Config load(String path) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = new BufferedReader(new FileReader(path));
        Config obj = gson.fromJson(br, Config.class);
        br.close();
        return obj;
    }

    public void save(String path, Config obj) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String content = gson.toJson(obj);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(path);
            pw.write(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (pw != null)
                pw.close();
        }
    }


}
