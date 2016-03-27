package by.bsu.kolodyuk.web.service;

import by.bsu.kolodyuk.model.User;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Api(value = "users", description = "User Service")
@Path("/api")
public class UserService {

    ConcurrentHashMap<Long, User> userDB = new ConcurrentHashMap<>();

    @GET
    @Path("/users")
    @Produces(APPLICATION_JSON)
    @ApiOperation("Get All Users")
    public Collection<User> getUsers() {
        return userDB.values();
    }

    @POST
    @Path("/users")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @ApiOperation("Add User")
    public User addUser(User user) {
        user.setId(userDB.size() + 1L);
        userDB.put(user.getId(), user);
        return user;
    }

    @GET
    @Path("/users/{id}")
    @Produces(APPLICATION_JSON)
    @ApiOperation("Get User")
    public User getUser(@PathParam("id") Long id) {
        return userDB.get(id);
    }

    @PUT
    @Path("/users/{id}")
    @Consumes(APPLICATION_JSON)
    @ApiOperation("Update User")
    public void updateUser(@PathParam("id") Long id, User user) {
        userDB.put(id, user);
    }

    @DELETE
    @Path("/users/{id}")
    @ApiOperation("Delete User")
    public void deleteUser(@PathParam("id") Long id) {
        userDB.remove(id);
    }

    @GET
    @Path("/users/{id}/logo")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ApiOperation(value = "Download User's Logo")
    public Response downloadLogo(@PathParam("id") Long id) {
        byte[] logo = userDB.get(id).getLogo();
        return Response.ok(new ByteArrayInputStream(logo), MediaType.APPLICATION_OCTET_STREAM)
                       .header("Content-Disposition", "attachment; filename=\"logo\"")
                       .build();
    }

    @POST
    @Path("/users/{id}/logo")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @ApiOperation("Upload User's Logo")
    public void uploadLogo(@PathParam("id") Long id, InputStream stream) throws IOException {
        userDB.get(id).setLogo(IOUtils.toByteArray(stream));
    }
}
