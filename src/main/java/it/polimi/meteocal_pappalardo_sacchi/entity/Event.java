/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author Alessandro
 */
@NamedQuery(name = "Event.findEventsToMonitorForWeather", query = "SELECT e FROM EVENT e WHERE e.isEventToMonitorForForecast = true")
@Entity(name="EVENT")
public class Event implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private boolean isPublic;
    private Time startingTime;
    private Time endingTime;
    private Date startingDate;
    private Date endingDate;
    private String addressWithoutCity;
    private String city;
    private boolean isEventToMonitorForForecast;
    
    @Embedded
    private WeatherCondition weatherCondition;

    @OneToMany(cascade = CascadeType.ALL)
    private List<WeatherForecast> weatherForecastList;

    @OneToMany(mappedBy = "relatedEvent", cascade = CascadeType.ALL)
    private List<Notification> relatedNotificationList;

    @ManyToOne
    private MeteoCalUser creator;

    @ManyToMany()
    @JoinTable(name="EVENT_CALENDAR")
    private List<Calendar> participantCalendarList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return title;
        //return "it.polimi.meteocal_pappalardo_sacchi.Event[ id=" + id + " ]";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Time getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Time startingTime) {
        this.startingTime = startingTime;
    }

    public Time getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(Time endingTime) {
        this.endingTime = endingTime;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }
    
    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public WeatherCondition getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(WeatherCondition weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public List<WeatherForecast> getWeatherForecastSet() {
        return weatherForecastList;
    }

    public void setWeatherForecastList(List<WeatherForecast> weatherForecastSet) {
        this.weatherForecastList = weatherForecastSet;
    }

    public List<Notification> getRelatedNotificationList() {
        return relatedNotificationList;
    }

    public void setRelatedNotificationList(List<Notification> relatedNotificationList) {
        this.relatedNotificationList = relatedNotificationList;
    }

    public MeteoCalUser getCreator() {
        return creator;
    }
    
    public void setCreator(MeteoCalUser creator) {
        this.creator = creator;
    }

    public List<Calendar> getParticipantCalendarList() {
        return participantCalendarList;
    }

    public void setParticipantCalendarList(List<Calendar> participantCalendarList) {
        this.participantCalendarList = participantCalendarList;
    }

    public String getAddressWithoutCity() {
        return addressWithoutCity;
    }

    public void setAddressWithoutCity(String addressWithoutCity) {
        this.addressWithoutCity = addressWithoutCity;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isEventToMonitorForForecast() {
        return isEventToMonitorForForecast;
    }

    public void setEventToMonitorForForecast(boolean isEventToMonitorForForecast) {
        this.isEventToMonitorForForecast = isEventToMonitorForForecast;
    }
}
