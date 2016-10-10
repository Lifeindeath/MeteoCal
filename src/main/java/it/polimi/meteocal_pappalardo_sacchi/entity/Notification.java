/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.Set;

/**
 *
 * @author Alessandro
 */
@Entity(name="NOTIFICATION")
public class Notification implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date generationDate;
    private Time generationTime;
    private String message;

    @ManyToOne(targetEntity=MeteoCalUser.class, optional=false)
    private MeteoCalUser sentToUser;
    
    @ManyToOne(targetEntity=Event.class, optional=false)
    private Event relatedEvent;
    
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
        if (!(object instanceof Notification)) {
            return false;
        }
        Notification other = (Notification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal_pappalardo_sacchi.Notification[ id=" + id + " ]";
    }

    public Date getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(Date generationDate) {
        this.generationDate = generationDate;
    }

    public Time getGenerationTime() {
        return generationTime;
    }

    public void setGenerationTime(Time generationTime) {
        this.generationTime = generationTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MeteoCalUser getSentToUser() {
        return sentToUser;
    }

    public void setSentToUser(MeteoCalUser sentToUser) {
        this.sentToUser = sentToUser;
    }

    public Event getRelatedEvent() {
        return relatedEvent;
    }

    public void setRelatedEvent(Event relatedEvent) {
        this.relatedEvent = relatedEvent;
    }
    
}
