/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.bean;
import it.polimi.meteocal_pappalardo_sacchi.entity.*;
import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * Attenzione ad importare javax.faces.view.ViewScoped, l'altro non è compatibile con un CDI bean (i.e. @Named)
 */

@Named
@ViewScoped
public class EventBean implements Serializable {

    @Inject
    ScheduleBean scheduleBean;
    @Inject
    EventManager eventManager;
    @Inject
    UserManager userManager;
    @Inject
    UserBean userBean;
    @Inject
    WeatherBean weatherBean;
    @Inject
    NotificationManager notificationManager;
    
    private Event event;
    private Event oldEvent;

    private List<MeteoCalUser> invitedUserListToNotify;
    private List<Calendar> invitedUserCalendarList;
    private List<MeteoCalUser> alreadyInvitedUserList;
    private String usernameToSearch;

    public EventBean(){
    }

    public Event getEvent() {
        if (event==null) {
            event = new Event();
        }
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String create() {
        //controllo correttezza campi
        if (!checkAll()) {
            return null;
        }
        //set bad weather condition
        event.setWeatherCondition(weatherBean.getWeatherCondition());
        //setto il creatore dell'evento
        event.setCreator(userBean.getUser());
        userBean.getUser().getCreatedEventList().add(event);
        //aggiungo il creatore alla lista degli invitati dell'evento
        addInvitedUserCalendarToInvitedList(userBean.getUser().getOwnedCalendar());
        //setto la lista dei (calendari) partecipanti
        event.setParticipantCalendarList(invitedUserCalendarList);
        //invio inviti
        sendInvitationToUserList();
        scheduleBean.setupForecastRetrievalForNewEvent(event, true);
        eventManager.create(event);
        scheduleBean.notifyInvitedUsersOfBadWeather();
        return "/user/calendar?faces-redirect=true";
    }

    public String delete() {
        sendDeletionNotification();
        eventManager.remove(event);
        return "/user/calendar?faces-redirect=true";
    }
    
    public String modify(){
        if (!checkAll()){
            return null;
        }
        
        //invio notifica di modifica
        sendModificationToUserList();        
        //aggiungo gli eventuali nuovi invitati
        if (invitedUserCalendarList != null) {
            event.getParticipantCalendarList().addAll(invitedUserCalendarList);
        }        
        //invio inviti
        sendInvitationToUserList();
        //persisto entità modificata
        eventManager.edit(event);
        return "/user/calendar?faces-redirect=true";
    }

    /*da vedere se serve*/
    public void preRenderView() {
        if (event == null) {
            event = new Event();
        }
    }

    /* setters and getters for time and date*/
    public java.util.Date getStartingTime() {
        return (java.util.Date) this.getEvent().getStartingTime();
    }

    public void setStartingTime(java.util.Date date) {
        //questo controllo serve per far comparire correttamente il messaggio di errore inserisci time
        if (date == null) {
            return;
        }
        this.getEvent().setStartingTime(new java.sql.Time(date.getTime()));
    }
    
    public java.util.Date getEndingTime() {
        return (java.util.Date) this.getEvent().getEndingTime();
    }

    public void setEndingTime(java.util.Date date) {
        //questo controllo serve per far comparire correttamente il messaggio di errore inserisci time
        if (date == null) {
            return;
        }
        this.getEvent().setEndingTime(new java.sql.Time(date.getTime()));
    }
    
    public java.util.Date getStartingDate() {
        return (java.util.Date) this.getEvent().getStartingDate();
    }

    public void setStartingDate(java.util.Date date) {
        //questo controllo serve per far comparire correttamente il messaggio di errore inserisci date
        if (date == null) {
            return;
        }
        this.getEvent().setStartingDate(new java.sql.Date(date.getTime()));
    }
    
    public java.util.Date getEndingDate() {
        return (java.util.Date) this.getEvent().getEndingDate();
    }

    public void setEndingDate(java.util.Date date) {
        //questo controllo serve per far comparire correttamente il messaggio di errore inserisci date
        if (date == null) {
            return;
        }
        this.getEvent().setEndingDate(new java.sql.Date(date.getTime()));
    }

    /* Methods to search and invite guests*/
    public void addInsertedUser() {
        if (usernameToSearch == null) {
            FacesMessage messageEmpty = new FacesMessage("Insert a username");
            FacesContext.getCurrentInstance().addMessage("eventForm:searchuser", messageEmpty);
            return;
        }
        MeteoCalUser userToAdd = userManager.find(usernameToSearch);
        //se non trovo un utente con quell'username
        if (userToAdd == null) {
            FacesMessage messageNotExist = new FacesMessage("User doesn't exist");
            FacesContext.getCurrentInstance().addMessage("eventForm:searchuser", messageNotExist);
            return;
          //se cerco di invitare me stesso
        } else if (userToAdd.equals(userBean.getUser())) {
            FacesMessage messageMyself = new FacesMessage("Inviting yourself doesn't make sense");
            FacesContext.getCurrentInstance().addMessage("eventForm:searchuser", messageMyself);
            return;
          //se ho già aggiunto l'utente alla lista degli invitati
        } else if (invitedUserListToNotify != null && invitedUserListToNotify.contains(userToAdd)) {
            FacesMessage messageAlready = new FacesMessage("User already invited");
            FacesContext.getCurrentInstance().addMessage("eventForm:searchuser", messageAlready);
            return;
            //se sto eseguendo la modifica di un evento e cerco di aggiungere un utente già invitato
        } else if (alreadyInvitedUserList != null && alreadyInvitedUserList.contains(userToAdd)){
            FacesMessage messageAlready = new FacesMessage("User previously invited");
            FacesContext.getCurrentInstance().addMessage("eventForm:searchuser", messageAlready);
            return;
        } else {
            FacesMessage messageExist = new FacesMessage("User found");
            FacesContext.getCurrentInstance().addMessage("eventForm:searchuser", messageExist);
            addInvitedUserToNotifyList(userToAdd);
            addInvitedUserCalendarToInvitedList(userToAdd.getOwnedCalendar());
            //nullifico la stringa dopo che viene presa corettamente
            usernameToSearch = null;
            return;
        }
    }

    private void addInvitedUserCalendarToInvitedList(Calendar calendar) {
        if (invitedUserCalendarList == null) {
            invitedUserCalendarList = new ArrayList<Calendar>();
        }
        invitedUserCalendarList.add(calendar);
    }

    private void addInvitedUserToNotifyList(MeteoCalUser user) {
        if (invitedUserListToNotify == null) {
            invitedUserListToNotify = new ArrayList<MeteoCalUser>();
        }
        invitedUserListToNotify.add(user);
    }

    private void sendInvitationToUserList() {
        java.sql.Date nowDate = new java.sql.Date(java.util.Calendar.getInstance().getTime().getTime());
        java.sql.Time nowTime = new java.sql.Time(java.util.Calendar.getInstance().getTime().getTime());
        if (invitedUserListToNotify == null) {
            return;
        }
        for (MeteoCalUser user : invitedUserListToNotify) {
            Invitation invitation = new Invitation();
            invitation.setGenerationDate(nowDate);
            invitation.setGenerationTime(nowTime);
            invitation.setRelatedEvent(event);
            invitation.setSentToUser(user);
            invitation.setMessage("Hello " + user.toString() + ", you have been invited to " + event.toString());
            if (event.getRelatedNotificationList() == null) {
                event.setRelatedNotificationList(new ArrayList<Notification>());
            }
            //metto in relazione l'invito con l'evento
            event.getRelatedNotificationList().add(invitation);
            //metto in relazione l'invito con l'utente
            user.getReceivedNotificationSet().add(invitation);
            
        }
        
    }

    public List<MeteoCalUser> getInvitedUserListToNotify() {
        return invitedUserListToNotify;
    }

    public void setInvitedUserListToNotify(List<MeteoCalUser> invitedUserListToNotify) {
        this.invitedUserListToNotify = invitedUserListToNotify;
    }

    public String getUsernameToSearch() {
        return usernameToSearch;
    }

    public void setUsernameToSearch(String usernameToSearch) {
        this.usernameToSearch = usernameToSearch;
    }

    public List<MeteoCalUser> getAlreadyInvitedUserList() {
        if (alreadyInvitedUserList == null) {
            alreadyInvitedUserList = eventManager.findAlreadyInvitedUserList(event);
        }
       return alreadyInvitedUserList;
    }

    //messo solo per sicurezza per jsf che ama avere sia getter che setter
    public void setAlreadyInvitedUserList(List<MeteoCalUser> alreadyInvitedUserList) {
        return;
    }
    
    /*modification notification*/

    public Event getOldEvent() {
        return oldEvent;
    }

    public void setOldEvent(Event oldEvent) {
        this.oldEvent = oldEvent;
    }
    
    private String getModifiedFields(){
        //NB poi tutte queste belle stringhe devono diventare costanti da dichiarare all'inizio della classe.
        //CleanCode says!
        //Mi sembra cosa buona e giusta!
        String message="";
        
        if(!event.getTitle().equals(oldEvent.getTitle())){
            message += "The new title is "+event.getTitle()+" \n";
        }
        if(!event.getDescription().equals(oldEvent.getDescription())){
            message += "The new descriprion is "+event.getDescription()+" \n";
        } 
        if(!event.getStartingTime().equals(oldEvent.getStartingTime())){
            message += "The new starting time is "+event.getStartingTime()+" \n";
        } 
        if(!event.getStartingDate().equals(oldEvent.getStartingDate())){
            message += "The new starting date is "+event.getStartingDate()+" \n";
        }
        if(!event.getEndingTime().equals(oldEvent.getEndingTime())){
            message += "The new ending time is "+event.getEndingTime()+" \n";
        } 
        if(!event.getEndingDate().equals(oldEvent.getEndingDate())){
            message += "The new ending date is "+event.getEndingDate()+" \n";
        }
        if(!event.getAddressWithoutCity().equals(oldEvent.getAddressWithoutCity())){
            message += "The new address is "+event.getAddressWithoutCity()+" \n";
        }
        
        if(!event.getCity().equals(oldEvent.getCity())){
            message += "The new city is "+event.getCity()+" \n";
        }
        
        return message;
    }
    
    private void sendModificationToUserList() {        
        List<MeteoCalUser> invitedGuests = eventManager.findAlreadyInvitedUserList(event);
        invitedGuests.remove(userBean.getUser());        
        
        if (invitedGuests.isEmpty()) {
            return;
        }
        
        if (getModifiedFields().equals("")){
            return;
        }
        
        String message = ".\n event "+oldEvent.getTitle()+" has been modified:\n"+getModifiedFields();
        
        for (MeteoCalUser user : invitedGuests) {
            Notification notification = composeNotification(user, message);
            
            notification.setRelatedEvent(event);
            if (event.getRelatedNotificationList() == null) {
                event.setRelatedNotificationList(new ArrayList<Notification>());
            }
            //metto in relazione la modifica con l'evento
            event.getRelatedNotificationList().add(notification);           
            notificationManager.create(notification);
        }
        
    }
    
    private Notification composeNotification(MeteoCalUser user, String message){
        java.sql.Date nowDate = new java.sql.Date(java.util.Calendar.getInstance().getTime().getTime());
        java.sql.Time nowTime = new java.sql.Time(java.util.Calendar.getInstance().getTime().getTime());
        
        Notification notification = new Notification();
        notification.setGenerationDate(nowDate);
        notification.setGenerationTime(nowTime);           
        notification.setSentToUser(user);
        notification.setMessage("Hi, " + user.getUsername() + message);
        //set relation with user
        user.getReceivedNotificationSet().add(notification); 
        
        return notification;
        
    }
    
    /*Deletion notification*/
    private void sendDeletionNotification() {
        
        List<MeteoCalUser> invitedGuests = eventManager.findAlreadyInvitedUserList(event);
        invitedGuests.remove(userBean.getUser());
        
        if (invitedGuests.isEmpty()) {
            return;
        }
               
        String message = ".\n event "+oldEvent.getTitle()+" has been deleted";
        
        for (MeteoCalUser user : invitedGuests) {
             notificationManager.create(composeNotification(user, message));
        }
    }
    /*Field checkers*/

    private boolean checkAll() {
        if (checkRequiredFieldsSet()) {
            return false;
        }
        if (checkDateOrder()) {
            return false;
        }
        if(checkTimeOrder()) {
            return false;
        }
        return true;
    }

    private boolean checkRequiredFieldsSet() {
        boolean areFieldsInvalid = false;
        if (getEvent().getTitle() == null) {
            FacesMessage messageTitle = new FacesMessage("Insert a title");
            FacesContext.getCurrentInstance().addMessage("eventForm:title", messageTitle);
            areFieldsInvalid = true;
        }

        if (getEvent().getDescription() == null) {
            FacesMessage messageDescription = new FacesMessage("Insert a description");
            FacesContext.getCurrentInstance().addMessage("eventForm:description", messageDescription);
            areFieldsInvalid = true;
        }

        if (getEvent().getAddressWithoutCity() == null) {
            FacesMessage messageAddress = new FacesMessage("Insert an address");
            FacesContext.getCurrentInstance().addMessage("eventForm:address", messageAddress);
            areFieldsInvalid = true;
        }
        
        if (getEvent().getCity() == null) {
            FacesMessage messageCity = new FacesMessage("Insert a city");
            FacesContext.getCurrentInstance().addMessage("eventForm:city", messageCity);
            areFieldsInvalid = true;
        }

        if (getEvent().getStartingTime() == null) {
            FacesMessage messageStartingTime = new FacesMessage("Insert a starting time");
            FacesContext.getCurrentInstance().addMessage("eventForm:startingtime", messageStartingTime);
            areFieldsInvalid = true;
        }

        if (getEvent().getEndingTime() == null) {
            FacesMessage messageEndingTime = new FacesMessage("Insert an ending time");
            FacesContext.getCurrentInstance().addMessage("eventForm:endingtime", messageEndingTime);
            areFieldsInvalid = true;
        }

        if (getEvent().getStartingDate() == null) {
            FacesMessage messageStartingDate = new FacesMessage("Insert a starting date");
            FacesContext.getCurrentInstance().addMessage("eventForm:startingdate", messageStartingDate);
            areFieldsInvalid = true;
        }

        if (getEvent().getEndingDate() == null) {
            FacesMessage messageEndingDate = new FacesMessage("Insert an ending date");
            FacesContext.getCurrentInstance().addMessage("eventForm:endingdate", messageEndingDate);
            areFieldsInvalid = true;
        }
        return areFieldsInvalid;
    }

    private boolean checkDateOrder() {
        if (getEvent().getStartingDate().after(getEvent().getEndingDate())) {
            FacesMessage message = new FacesMessage("The starting date can't be after the ending date");
            FacesContext.getCurrentInstance().addMessage("eventForm:startingdate", message);
            return true;
        }
        return false;
    }

    private boolean checkTimeOrder() {
        if (getEvent().getStartingDate().equals(getEvent().getEndingDate()) && getEvent().getStartingTime().after(getEvent().getEndingTime())) {
            FacesMessage message = new FacesMessage("The starting time can't be after the ending time on the same day");
            FacesContext.getCurrentInstance().addMessage("eventForm:startingtime", message);
            return true;
        }
        return false;
    }
    
}
