package tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

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
        File f = new File(path);
        System.out.println("Save as : " + f.getAbsolutePath());
        String content = this.gson.toJson(obj);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        pw.write(content);
        pw.close();
    }

    public T load(String path) throws IOException {
        File f = new File(path);
        System.out.println("Load from : " + f.getAbsolutePath());
        BufferedReader br = new BufferedReader(new FileReader(path));
        T obj = (T) this.gson.fromJson(br, this.c);
        br.close();
        return obj;
    }
}
