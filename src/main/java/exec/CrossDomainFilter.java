package exec;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import javax.ws.rs.core.Response;

/**
 * Created by Julien on 24.05.15.
 */
public class CrossDomainFilter implements ContainerResponseFilter {
    /**
     * Add the cross domain data to the output if needed
     *
     * @param creq The container request (input)
     * @param cres The container request (output)
     * @return The output request with cross domain if needed
     */
    @Override
    public ContainerResponse filter(ContainerRequest creq, ContainerResponse cres) {
        Response.ResponseBuilder resp = Response.fromResponse(cres.getResponse());
        resp.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

        String reqHead = creq.getHeaderValue("Access-Control-Request-Headers");

        if (null != reqHead && !reqHead.equals("")) {
            resp.header("Access-Control-Allow-Headers", reqHead);
        }

        cres.setResponse(resp.build());
        return cres;
    }
}