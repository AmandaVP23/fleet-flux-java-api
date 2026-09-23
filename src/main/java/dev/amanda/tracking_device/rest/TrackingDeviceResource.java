package dev.amanda.tracking_device.rest;

import dev.amanda.tracking_device.domain.TrackingDevice;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/tracking-devices")
public class TrackingDeviceResource {
    @GET
    public String testing() {
        return "Hello! You're authenticated and you're a super admin";
    }

//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response create(TrackingDevice trackingDevice) {
//
//    }

    // todo - endpoint to assign device -> vehicle
}
