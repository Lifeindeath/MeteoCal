/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.entity;

import it.polimi.meteocal_pappalardo_sacchi.bean.PasswordEncrypter;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author Elena
 */
@NamedQuery(name = "MeteoCalUser.findSameUsername", query = "SELECT u.username FROM METEOCAL_USER u WHERE u.username = :username")
@Entity(name="METEOCAL_USER")
public class MeteoCalUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String email;

    @Column(nullable = false)
    private String groupName;

    @OneToMany(mappedBy="creator", targetEntity=Event.class, cascade = CascadeType.ALL)
    private List<Event> createdEventList;

    @OneToMany(mappedBy="sentToUser", targetEntity=Notification.class, cascade = CascadeType.ALL)
    private Set<Notification> receivedNotificationSet;

    @OneToOne(optional=false, cascade = CascadeType.ALL)
    private Calendar ownedCalendar;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MeteoCalUser)) {
            return false;
        }
        MeteoCalUser other = (MeteoCalUser) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PasswordEncrypter.encryptPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Event> getCreatedEventList() {
        return createdEventList;
    }

    public void setCreatedEventList(List<Event> createdEventList) {
        this.createdEventList = createdEventList;
    }

    public Set<Notification> getReceivedNotificationSet() {
        return receivedNotificationSet;
    }

    public void setReceivedNotificationSet(Set<Notification> receivedNotificationSet) {
        this.receivedNotificationSet = receivedNotificationSet;
    }

    public Calendar getOwnedCalendar() {
        return ownedCalendar;
    }

    public void setOwnedCalendar(Calendar ownedCalendar) {
        this.ownedCalendar = ownedCalendar;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
