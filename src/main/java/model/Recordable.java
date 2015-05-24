package model;


import tools.FileAccesss;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Julien on 24.05.15.
 */
public class Recordable<T> {

    private T data = null;
    private FileAccesss<T> fileAccesss = null;
    private String path = null;

    public Recordable(String path, Class c) {
        this.path = path;
        this.fileAccesss = new FileAccesss<T>(c);

        try {
            this.data = this.fileAccesss.load(this.path);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            try {
                this.data = (T) c.getConstructors()[0].newInstance();
            } catch (InstantiationException e1) {
                System.err.println(e1.getMessage());
            } catch (IllegalAccessException e1) {
                System.err.println(e1.getMessage());
            } catch (InvocationTargetException e1) {
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
