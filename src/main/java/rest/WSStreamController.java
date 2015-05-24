package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Config;
import worker.StreamManager;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static model.Config.setInstance;

/**
 * Created by Julien on 23.05.15.
 */

@Path("/stream")
public class WSStreamController {

    private StreamManager streamManager = StreamManager.getInstance();

    @GET
    @Path("/start")
    public Response start() {
        if(streamManager.startStream(Config.getInstance()))
            return Response.status(200).build();
        return Response.status(404).build();
    }

    @GET
    @Path("/stop")
    public Response stop() {
        if(streamManager.stopStream())
            return Response.status(200).build();
        return Response.status(404).build();
    }

    @POST
    @Path("/update")
    @Produces("application/json")
    @Consumes("application/json")
    public Response stop(String json) {
        streamManager.stopStream();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Config c = (Config) gson.fromJson(json, Config.CLASS);
        setInstance(c);

        if(c==null)
            return Response.status(404).build();

        streamManager.startStream(Config.getInstance());

        return Response.status(200).build();
    }


}
