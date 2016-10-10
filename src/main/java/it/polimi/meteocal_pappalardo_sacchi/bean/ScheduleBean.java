package it.polimi.meteocal_pappalardo_sacchi.bean;

import it.polimi.meteocal_pappalardo_sacchi.entity.Event;

import javax.ejb.Schedule;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alessandro on 09/01/15.
 * Application scoped means it's shared by the whole webapp and its lifetime is bound to the server lifetime.
 */
@Named(value = "scheduleBean")
@ApplicationScoped
public class ScheduleBean implements Serializable {

    @Inject
    ForecastBean forecastBean;
    @Inject
    EventManager eventManager;

    private List<Event> eventsToMonitorForForecast;

    public ScheduleBean() {
    }

    public void addEventToMonitorForForecast(Event event) {
        getEventsToMonitorForForecast().add(event);
    }

    /**
     *
     * @param event the event for which the forecasts are retrieved
     * @param isEventCreation is the method called during the event creation
     * @return true if the forecast retrieval is successful, false otherwise
     */

    public boolean setupForecastRetrievalForNewEvent(Event event, boolean isEventCreation) {
        if (forecastBean.retrieveMultipleForecast(event.getCity(), event.getStartingDate(), event.getEndingDate())) {
            event.setWeatherForecastList(forecastBean.getWeatherForecastList());
            event.setEventToMonitorForForecast(false);
            return true;
        } else if (isEventCreation) {
            event.setEventToMonitorForForecast(true);
            addEventToMonitorForForecast(event);
            return false;
        } else {
            return false;
        }
    }

    @Schedule(dayOfWeek = "*", timezone = "Europe/Paris")
    private void retrieveForecastForEventsToMonitor() {
        if (getEventsToMonitorForForecast().isEmpty()) {
            return;
        }
        for (Event e : eventsToMonitorForForecast) {
            //se la previsione esiste
            if (setupForecastRetrievalForNewEvent(e, false)) {
                //tolgo l'evento dalla lista degli eventi da monitorare
                eventsToMonitorForForecast.remove(e);
                //persisto la modifica dell'evento sul db
                eventManager.edit(e);
            }
        }
    }

    @Schedule(dayOfWeek = "*", timezone = "Europe/Paris")
    public void notifyInvitedUsersOfBadWeather() {
        List<Event> ev = new ArrayList<Event>();
        ev = eventManager.findEventsWithBadWeatherIn24HoursOrLess();
        for(Event e:ev){
            System.out.println(e.getTitle()+" da notificare ");            
        }
    }

    private List<Event> getEventsToMonitorForForecast() {
        //se la lista è vuota, per esempio ad avvio server, la riempio con gli eventi da monitorare tratti dal db
        if (eventsToMonitorForForecast == null) {
            eventsToMonitorForForecast = new ArrayList<Event>();
            eventsToMonitorForForecast.addAll(eventManager.findEventsToMonitorForForecast());
        }
        return eventsToMonitorForForecast;
    }

}
