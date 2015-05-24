package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Config;
import model.TweetCollection;
import worker.StreamManager;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Julien on 23.05.15.
 */

@Path("/stream")
public class WSStreamController {

    private StreamManager streamManager = StreamManager.getInstance();

    @GET
    @Path("/start")
    public Response start() {
        System.out.println("WS:Start");
        System.out.println(Config.getInstance());
        if (streamManager.startStream(Config.getInstance()))
            return Response.status(200).build();
        return Response.status(404).build();
    }

    @GET
    @Path("/stop")
    public Response stop() {
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
    public Response putConfig(String json) {

        System.out.println("WS:PutConfig");
        System.out.println(json);
        System.out.println(Config.getInstance().toString());

        streamManager.stopStream();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        HashMap<String, List<String>> c = null;
        c = gson.fromJson(json, new TypeToken<HashMap<String, List<String>>>() {
        }.getType());
        //setInstance(c);




        if (c == null)
            return Response.status(404).build();

        streamManager.startStream(Config.getInstance());

        return Response.status(200).build();
    }

    @GET
    @Path("/config")
    @Produces("application/json")
    public Response getConfig() {
        System.out.println("WS:GetConfig");
        String content = Config.getInstance().toString();
        System.out.println(content);
        return Response.status(200).entity(content).build();
    }


}
