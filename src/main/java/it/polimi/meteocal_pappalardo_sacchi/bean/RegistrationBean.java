/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.bean;

import it.polimi.meteocal_pappalardo_sacchi.entity.Group;
import it.polimi.meteocal_pappalardo_sacchi.entity.Calendar;
import it.polimi.meteocal_pappalardo_sacchi.entity.MeteoCalUser;


import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;



/**
 *
 *
 */
@Named(value = "registrationBean")
@RequestScoped
public class RegistrationBean {

    @Inject
    private UserManager userManager;
    
    private MeteoCalUser user;
    private Calendar calendar;
    
    public MeteoCalUser getUser() {
        if (user == null) {
            user = new MeteoCalUser();
            user.setGroupName(Group.USER);
            if (calendar == null) {
                calendar = new Calendar();
                user.setOwnedCalendar(calendar);
                calendar.setOwner(user);
            }
            user.setGroupName(Group.USER);
        }
        return user;
    }
    
    public String register() {
        if (userManager.isUsernameUnique(user.getUsername())) {
            userManager.create(user);
            return null;
        }
        FacesMessage message = new FacesMessage("Username already exists");
        FacesContext.getCurrentInstance().addMessage("registrationForm:username", message);
        return null;
    }
}
