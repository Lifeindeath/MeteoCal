/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.bean;


import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import it.polimi.meteocal_pappalardo_sacchi.entity.Calendar;
import it.polimi.meteocal_pappalardo_sacchi.entity.Event;
import it.polimi.meteocal_pappalardo_sacchi.entity.MeteoCalUser;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Named(value = "calendarBean")
@ViewScoped
public class CalendarBean implements Serializable {

    @Inject
    private CalendarManager calendarManager;
    @Inject
    private UserBean userBean;
    @Inject
    private UserManager userManager;

    private Calendar calendar;
    private String usernameToSearch;

    private List<Event> allSearchedEventList;

    //la variabile è vera se sto visualizzando i risultati di una ricerca
    private boolean isSearching;

    public Calendar getCalendar() {
        if (calendar == null) {
            calendar = userBean.getUser().getOwnedCalendar();
        }
        return calendar;
    }

    public void searchEventListByUser() {
        if (usernameToSearch == null) {
            //metto stop search in modo che se cerco di passare da una ricerca valida ad una non valida oltre a dirmi che la nuova ricerca non è valida azzera quella corrente
            stopSearch();
            FacesMessage messageEmpty = new FacesMessage("Insert a username");
            FacesContext.getCurrentInstance().addMessage("searchForm:searchuser", messageEmpty);
            return;
        }
        MeteoCalUser userToSearch = userManager.find(usernameToSearch);
        if (userToSearch == null) {
            stopSearch();
            FacesMessage messageNotExist = new FacesMessage("User doesn't exist");
            FacesContext.getCurrentInstance().addMessage("searchForm:searchuser", messageNotExist);
            return;
        }
        if (!userToSearch.getOwnedCalendar().isIsPublic()) {
            stopSearch();
            FacesMessage messagePrivate = new FacesMessage("User calendar is private");
            FacesContext.getCurrentInstance().addMessage("searchForm:searchuser", messagePrivate);
            return;
        }
        allSearchedEventList = calendarManager.findJoinedEventsByUserAndDate(userToSearch, convertJavaDateToSqlDate(getPickedDate()));
        isSearching = true;
        return;
    }

    public void stopSearch() {
        usernameToSearch = null;
        allSearchedEventList = null;
        isSearching = false;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public java.util.Date getToday() {
        return java.util.Calendar.getInstance().getTime();
    }

    public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    public java.util.Date getPickedDate() {
        if (userBean.getPickedDate() == null) {
            userBean.setPickedDate(getToday());
        }
        return userBean.getPickedDate();
    }

    public void setPickedDate(Date pickedDate) {
        userBean.setPickedDate(pickedDate);
    }
    
    public List<Event> getEventsByPickedDate() {
        return calendarManager.findJoinedEventsByUserAndDate(userBean.getUser(), convertJavaDateToSqlDate(getPickedDate()));
    }

    public void updatePrivacySetting() {
        calendarManager.edit(calendar);
    }

    public String getUsernameToSearch() {
        return usernameToSearch;
    }

    public void setUsernameToSearch(String usernameToSearch) {
        this.usernameToSearch = usernameToSearch;
    }

    public List<Event> getAllSearchedEventList() {
        //se non metto il controllo potrei scrivere il nome dell'utente da cercare, cambiare data, e partirebbe la query anche se non è necessario
        if (isSearching) {
            searchEventListByUser();
        }
        return allSearchedEventList;
    }

    public void setAllSearchedEventList(List<Event> allSearchedEventList) {
        this.allSearchedEventList = allSearchedEventList;
    }

    public boolean isSearching() {
        return isSearching;
    }

    public void setSearching(boolean isSearching) {
        this.isSearching = isSearching;
    }
}
