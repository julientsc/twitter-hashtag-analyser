package exec;

import model.Config;
import worker.StreamManager;

/**
 * Created by Julien on 24.05.15.
 */
public class Init {
    public Init() {
        System.out.println("Initialization");
        StreamManager.getInstance().startStream(Config.getInstance());
    }
}
