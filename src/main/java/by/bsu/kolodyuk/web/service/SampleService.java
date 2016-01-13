package by.bsu.kolodyuk.web.service;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api(value = "/sample", description = "Sample Service")
@Path("/sample")
public class SampleService
{
    @GET
    @Path("/dummy")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @ApiOperation(value = "Dummy operation")
    public String getDummy()
    {
        return "Dummy";
    }

}
