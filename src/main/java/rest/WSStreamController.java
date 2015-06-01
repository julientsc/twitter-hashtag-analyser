package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Config;
import model.TweetCollection;
import worker.StreamManager;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by Julien on 23.05.15.
 */

@Path("/stream")
public class WSStreamController {

    private StreamManager streamManager = StreamManager.getInstance();

    @GET
    @Path("/start")
    synchronized public Response start() {
        System.out.println("WS:Start");
        System.out.println(Config.getInstance());
        if (streamManager.startStream(Config.getInstance())) {
            return Response.status(200).build();
        }
        return Response.status(404).build();
    }

    @GET
    @Path("/stop")
    synchronized public Response stop() {
        System.out.println("WS:Stop");
        if (streamManager.stopStream()) {
            try {
                TweetCollection.getInstance().save();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            return Response.status(200).build();
        }
        return Response.status(404).build();
    }

    @POST
    @Path("/config")
    @Produces("application/json")
    @Consumes("application/json")
    synchronized public Response putConfig(String json) {

        System.out.println("WS:PutConfig");

        boolean isEnabled = StreamManager.getInstance().isWorking();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Config config = gson.fromJson(json, Config.class);

        if (config == null) {
            return Response.status(404).build();
        }

        if (isEnabled)
            streamManager.stopStream();

        Config.setInstance(config);

        if (isEnabled)
            streamManager.startStream(Config.getInstance());

        return Response.status(200).build();
    }

    @GET
    @Path("/config")
    @Produces("application/json")
    synchronized public Response getConfig() {
        System.out.println("WS:GetConfig");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String content = gson.toJson(Config.getInstance());
        System.out.println(content);
        return Response.status(200).entity(content).build();
    }


}
