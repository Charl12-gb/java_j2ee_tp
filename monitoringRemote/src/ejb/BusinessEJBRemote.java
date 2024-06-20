/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ejb;

import jakarta.ejb.Remote;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 *
 * @author gboyo
 */
@Remote
public interface BusinessEJBRemote {
    void registerAlert(Date raiseTime, String platform, String target, String source, Date sendTime, int code, String desc, String statut);
    List<Object> listAlerts(String platform, LocalDateTime start, LocalDateTime end);
    void updateAlert(Long id, Date raiseTime, String platform, String target, String source, Date sendTime, int code, String desc, String statut);
}
