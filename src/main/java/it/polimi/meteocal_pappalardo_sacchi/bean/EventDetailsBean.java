package it.polimi.meteocal_pappalardo_sacchi.bean;

import it.polimi.meteocal_pappalardo_sacchi.entity.Event;
import it.polimi.meteocal_pappalardo_sacchi.entity.Invitation;

import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 *
 */
@Named
@ViewScoped
public class EventDetailsBean implements Serializable {

    @Inject
    UserBean userBean;
    @Inject
    NotificationManager notificationManager;
    @Inject
    EventManager eventManager;

    Invitation invitation;
    Event event;

    public void updateAcceptance() {
        notificationManager.edit(invitation);
    }

    public Invitation getInvitation() {
        if (invitation == null) {
            invitation = notificationManager.findInvitationByUserAndEvent(userBean.getUser(), event);
        }
        return invitation;
    }

    public void setInvitation(Invitation invitation) {
        this.invitation = invitation;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

}
