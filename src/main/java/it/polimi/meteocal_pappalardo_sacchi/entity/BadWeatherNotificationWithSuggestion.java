/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;


/**
 *
 * @author Alessandro
 */
@Entity
public class BadWeatherNotificationWithSuggestion extends BadWeatherNotification implements Serializable {
    @Id
    private Date newStartingDate;
    private Date newEndingDate;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BadWeatherNotificationWithSuggestion)) {
            return false;
        }
        BadWeatherNotificationWithSuggestion other = (BadWeatherNotificationWithSuggestion) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal_pappalardo_sacchi.Invitation[ id=" + this.getId() + " ]";
    }

    public Date getNewStartingDate() {
        return newStartingDate;
    }

    public void setNewStartingDate(Date newStartingDate) {
        this.newStartingDate = newStartingDate;
    }

    public Date getNewEndingDate() {
        return newEndingDate;
    }

    public void setNewEndingDate(Date newEndingDate) {
        this.newEndingDate = newEndingDate;
    }
    
}
