package AlertBusiness;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import jakarta.annotation.Priority;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 *
 * @author gboyo
 */
@Provider
@Priority(Priorities.USER)
public class AlertInterceptor implements ContainerRequestFilter {

    @PersistenceContext
    private EntityManager em;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void filter(ContainerRequestContext requestContext) {
        try {
            System.out.println("Je suis dedans");
            if (requestContext.getMethod().equalsIgnoreCase("POST") &&
                requestContext.getUriInfo().getPath().contains("register")) {
                
                System.out.println("Je suis dans le if");
                
                InputStream inputStream = requestContext.getEntityStream();
                String json = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));
                
                System.out.println("JSON reçu: " + json);
                
                Alerte alerte = objectMapper.readValue(json, Alerte.class);
                
                System.out.println("Code: " + alerte.getCode());

                try {
                    Correspondance correspondance = em.createQuery(
                        "SELECT c FROM Correspondance c WHERE c.code = :code", Correspondance.class)
                        .setParameter("code", alerte.getCode())
                        .getSingleResult();

                    if (correspondance != null) {
                        alerte.setTarget(correspondance.getTarget());
                        System.out.println("Correspondance trouvée: " + correspondance.getTarget());
                        requestContext.setEntityStream(new ByteArrayInputStream(
                            objectMapper.writeValueAsBytes(alerte)));
                    } else {
                        System.out.println("Aucune correspondance trouvée pour le code: " + alerte.getCode());
                    }
                } catch (NoResultException e) {
                    System.out.println("Aucune correspondance trouvée pour le code: " + alerte.getCode());
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur inattendue: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
