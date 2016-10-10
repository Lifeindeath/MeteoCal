/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Elena
 */
@Entity(name="CALENDAR")
public class Calendar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private boolean isPublic;


    @OneToOne(optional=false, cascade = CascadeType.ALL)
    private MeteoCalUser owner;

    /*
    We choose the eager policy because when I load the calendar I want all the events
    on it to be loaded togheter with it.
    */

    @ManyToMany(mappedBy= "participantCalendarList", fetch=FetchType.EAGER)
    private List<Event> joinedEventList;

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
        if (!(object instanceof Calendar)) {
            return false;
        }
        Calendar other = (Calendar) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal_pappalardo_sacchi.Calendar[ id=" + id + " ]";
    }

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public MeteoCalUser getOwner() {
        return owner;
    }

    public void setOwner(MeteoCalUser owner) {
        this.owner = owner;
    }

    public List<Event> getJoinedEventList() {
        return joinedEventList;
    }

    public void setJoinedEventList(List<Event> joinedEventList) {
        this.joinedEventList = joinedEventList;
    }

}
