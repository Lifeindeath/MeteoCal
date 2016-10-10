/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.bean;

import it.polimi.meteocal_pappalardo_sacchi.entity.Event;
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
public class EventManager extends AbstractManager<Event> {
    @PersistenceContext(unitName = "authPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventManager() {
        super(Event.class);
    }

    public List<MeteoCalUser> findAlreadyInvitedUserList(Event event) {
           
        String queryString = "SELECT u " +
                            "FROM CALENDAR c JOIN METEOCAL_USER u " +
                            "WHERE :event = c.joinedEventList " +
                            "AND c.owner = u";

        Query query = em.createQuery(queryString);        
        query.setParameter("event", event);
        return query.getResultList();
        
    }

    public List<Event> findEventsWithBadWeatherIn24HoursOrLess() {
        String queryString = "SELECT e " +
                            "FROM EVENT e JOIN WF w " +
                            "WHERE w = e.weatherForecastList " +
                            "AND (w.weatherCondition.temperature < e.weatherCondition.temperature "+
                            "OR w.weatherCondition.windStrength > e.weatherCondition.windStrength "+
                            "OR w.weatherCondition.precipitationType = e.weatherCondition.precipitationType)";
           
                

        Query query = em.createQuery(queryString);        
        return query.getResultList();
    }

    public List<Event> findEventsToMonitorForForecast() {
        return em.createNamedQuery("Event.findEventsToMonitorForWeather").getResultList();
    }
}
