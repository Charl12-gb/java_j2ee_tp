/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package AlertBusiness;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 *
 * @author gboyo
 */
@LocalBean
@Stateless
public class BuninessEJB implements BuninessEJBRemote {

    @PersistenceContext
    private EntityManager em;

    public void registerAlert(Date raiseTime, String platform, String target, String source, Date sendTime, int code, String description, String statut) {
        Alerte alerte = new Alerte();

        try {
            Correspondance correspondance = em.createQuery(
                "SELECT c FROM Correspondance c WHERE c.code = :code", Correspondance.class)
                .setParameter("code", code)
                .getSingleResult();

            if (correspondance != null) {
                target = correspondance.getTarget(); 
                System.out.println("Correspondance trouvée: " + correspondance.getTarget());
            }
        } catch (NoResultException e) {
            System.out.println("Aucune correspondance trouvée pour le code: " + code);
        }

        alerte.setRaiseTime(raiseTime);
        alerte.setPlatform(platform);
        alerte.setTarget(target); 
        alerte.setSource(source);
        alerte.setSendTime(sendTime);
        alerte.setCode(code);
        alerte.setDescription(description);
        alerte.setStatut(statut);
        em.persist(alerte);
    }

    public void updateAlert(Long id, Date raiseTime, String platform, String target, String source, Date sendTime, int code, String description, String statut) {
        Alerte alert = em.find(Alerte.class, id);
        if (alert != null) {
            alert.setRaiseTime(raiseTime);
            alert.setPlatform(platform);
            alert.setTarget(target);
            alert.setSource(source);
            alert.setSendTime(sendTime);
            alert.setCode(code);
            alert.setDescription(description);
            alert.setStatut(statut);
            em.merge(alert);
        } else {
            throw new NoResultException("Alert with ID " + id + " not found.");
        }
    }

    public List<Alerte> listAlerts(String platform, Date start, Date end) {
        return em.createQuery("SELECT a FROM Alerte a WHERE a.platform LIKE :platform AND a.raiseTime BETWEEN :start AND :end", Alerte.class)
                 .setParameter("platform", "%" + platform + "%")
                 .setParameter("start", start)
                 .setParameter("end", end)
                 .getResultList();
    }
}
