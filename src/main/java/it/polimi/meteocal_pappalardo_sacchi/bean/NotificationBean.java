package it.polimi.meteocal_pappalardo_sacchi.bean;

import it.polimi.meteocal_pappalardo_sacchi.entity.Notification;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 *
 */
@Named
@ViewScoped
public class NotificationBean implements Serializable {

    @Inject
    private NotificationManager notificationManager;
    @Inject
    private UserBean userBean;
    
    public List<Notification> getNotificationByUser() {
        return notificationManager.findNotificationByUser(userBean.getUser());
    }
    
}
