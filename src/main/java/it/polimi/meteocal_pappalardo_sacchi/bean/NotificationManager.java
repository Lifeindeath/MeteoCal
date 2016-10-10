/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.bean;

import it.polimi.meteocal_pappalardo_sacchi.entity.Event;
import it.polimi.meteocal_pappalardo_sacchi.entity.Invitation;
import it.polimi.meteocal_pappalardo_sacchi.entity.MeteoCalUser;
import it.polimi.meteocal_pappalardo_sacchi.entity.Notification;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
@Stateless
public class NotificationManager extends AbstractManager<Notification> {
    @PersistenceContext(unitName = "authPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotificationManager() {
        super(Notification.class);
    }

    public List<Notification> findNotificationByUser(MeteoCalUser user) {
        String queryString = "SELECT n " +
                       "FROM NOTIFICATION n " +
                       "WHERE n.sentToUser = :user";
        Query query = em.createQuery(queryString);
        query.setParameter("user", user);        
        return query.getResultList();
    }

    public Invitation findInvitationByUserAndEvent(MeteoCalUser user, Event event) {
        String queryString = "SELECT i " +
                "FROM Invitation i " +
                "WHERE i.sentToUser = :user AND i.relatedEvent = :event";
        Query query = em.createQuery(queryString);
        query.setParameter("user", user);
        query.setParameter("event", event);
        return (Invitation) query.getSingleResult();
    }
}
