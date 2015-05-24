package model;


import tools.FileAccesss;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Julien on 24.05.15.
 */
public class Recordable<T> {

    private T data = null;
    private FileAccesss<T> fileAccesss = null;
    private String path = null;

    public Recordable(String path, T initValue) {
        this.path = path;
        this.fileAccesss = new FileAccesss<T>(initValue.getClass());

        try {
            this.data = this.fileAccesss.load(this.path);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            this.data = initValue;
            try {
                save();
            } catch (FileNotFoundException e1) {
                System.err.println(e1.getMessage());
            }
        }
    }

    public void save() throws FileNotFoundException {
        fileAccesss.save(path, data);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
