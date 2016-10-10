/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.bean;

import it.polimi.meteocal_pappalardo_sacchi.entity.Calendar;
import it.polimi.meteocal_pappalardo_sacchi.entity.Event;
import it.polimi.meteocal_pappalardo_sacchi.entity.MeteoCalUser;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Stateless
public class CalendarManager extends AbstractManager<Calendar> {
    @PersistenceContext(unitName = "authPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CalendarManager() {
        super(Calendar.class);
    }

    public List<Event> findJoinedEventsByUserAndDate(MeteoCalUser user, java.sql.Date date) {
        //distinct serve per evitare di avere duplicati dato che la condizione OR e.creator = :user combinata con la JOIN invitation induce ad avere più righe uguali relative a inviti inviati a utenti diversi
        String queryString = "SELECT DISTINCT e " +
                       "FROM EVENT e JOIN CALENDAR c " +// JOIN Invitation i " +
                       "WHERE c = e.participantCalendarList " +
                       "AND c.owner = :user " +
                       "AND :date BETWEEN e.startingDate and e.endingDate " +
                       "AND e.creator = :user";
                //   + "OR ( i.sentToUser = :user AND i.relatedEvent = e AND i.doesParticipate = true ))";                  

        Query query = em.createQuery(queryString);
        query.setParameter("user", user);
        query.setParameter("date", date);
        List<Event> ev = query.getResultList();
        
        queryString = "SELECT DISTINCT e " +
                       "FROM EVENT e JOIN CALENDAR c JOIN Invitation i " +
                       "WHERE c = e.participantCalendarList " +
                       "AND c.owner = :user " +
                       "AND :date BETWEEN e.startingDate and e.endingDate " +
                       "AND i.sentToUser = :user AND i.relatedEvent = e AND i.doesParticipate = true"; 
         
        query = em.createQuery(queryString);
        query.setParameter("user", user);
        query.setParameter("date", date);
        ev.addAll(query.getResultList()); 
        
        return ev;
    }
}
