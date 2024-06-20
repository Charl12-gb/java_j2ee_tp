/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PackageWeb;

import AlertBusiness.Alerte;
import AlertBusiness.BuninessEJB;
import jakarta.ejb.EJB;
import jakarta.faces.annotation.RequestMap;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author gboyo
 */
@Path("")
public class AlertResource {
    // java:global/AlertApp/AlertBusinessEJB/BuninessEJB, java:global/AlertApp/AlertBusinessEJB/BuninessEJB!AlertBusiness.BuninessEJB
    private static final String JDNI_NAME = "java:global/AlertApp/AlertBusinessEJB/BuninessEJB!AlertBusiness.BuninessEJB";
    @EJB(lookup = JDNI_NAME)
    private BuninessEJB businessEJB;
    
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerAlert(@RequestMap Alerte alerte) {
        // Date raiseTime, String platform, String target, String source, Date sendTime, int code, String desc, String statut
        if (businessEJB == null) {
            try {
                businessEJB = (BuninessEJB) new InitialContext().lookup(JDNI_NAME);
            } catch (NamingException ex) {
                Logger.getLogger(AlertResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        businessEJB.registerAlert(alerte.getRaiseTime(), alerte.getPlatform(), alerte.getTarget(), alerte.getSource(), alerte.getSendTime(), alerte.getCode(), alerte.getDesccription(), alerte.getStatut());
        return Response.ok(Map.of("success", true, "data", "Alerte crée avec succès")).build();
    }

    @POST
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAlert(@PathParam("id") Long id, Alerte alerte) {
        alerte.setId(id);
        if (businessEJB == null) {
            try {
                businessEJB = (BuninessEJB) new InitialContext().lookup(JDNI_NAME);
            } catch (NamingException ex) {
                Logger.getLogger(AlertResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        businessEJB.updateAlert(id, alerte.getRaiseTime(), alerte.getPlatform(), alerte.getTarget(), alerte.getSource(), alerte.getSendTime(), alerte.getCode(), alerte.getDesccription(), alerte.getStatut());
        return Response.ok(Map.of("success", true, "data", "Alerte modifié avec succès")).build();
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAlerts(@QueryParam("platform") String platform,
                               @QueryParam("start") @DefaultValue("") String start,
                               @QueryParam("end") @DefaultValue("") String end) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Parse the start and end dates
            Date startDate = start.isEmpty() ? null : formatter.parse(start);
            Date endDate = end.isEmpty() ? new Date() : formatter.parse(end);
            System.out.println("start => " + startDate);

            if (businessEJB == null) {
                try {
                    businessEJB = (BuninessEJB) new InitialContext().lookup(JDNI_NAME);
                } catch (NamingException ex) {
                    Logger.getLogger(AlertResource.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            // Call the listAlerts method of BusinessEJB
            List<Alerte> alerts = businessEJB.listAlerts(platform, startDate, endDate);

            return Response.ok(Map.of("success", true, "data", alerts)).build();
        } catch (Exception e) {
            System.out.println("Error => " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", true, "message", e.getMessage())).build();
        }
    }
}
