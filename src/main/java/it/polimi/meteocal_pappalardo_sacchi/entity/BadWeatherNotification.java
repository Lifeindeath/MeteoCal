/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.entity;

import javax.persistence.Entity;
import java.io.Serializable;


/**
 *
 * @author Alessandro
 */
@Entity
public class BadWeatherNotification extends Notification implements Serializable {

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BadWeatherNotification)) {
            return false;
        }
        BadWeatherNotification other = (BadWeatherNotification) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal_pappalardo_sacchi.Invitation[ id=" + this.getId() + " ]";
    }
    
}
