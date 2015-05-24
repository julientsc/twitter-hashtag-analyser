package tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Julien on 24.05.15.
 */
public class FileAccesss<T> {

    private Gson gson = null;
    private Class c = null;

    public FileAccesss(Class c) {
        this.c = c;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void save(String path, T obj) {
        String content = this.gson.toJson(obj);
        PrintWriter pw = new PrintWriter(path);
        pw.write(content);
        pw.close();
    }

    public T load(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        T obj = (T) this.gson.fromJson(br, this.c);
        br.close();
        return obj;
    }
}