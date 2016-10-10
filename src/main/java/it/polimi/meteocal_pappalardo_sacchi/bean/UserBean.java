/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.bean;

import it.polimi.meteocal_pappalardo_sacchi.entity.Event;
import it.polimi.meteocal_pappalardo_sacchi.entity.MeteoCalUser;

import java.io.Serializable;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named(value = "userBean")
@SessionScoped
public class UserBean implements Serializable {

    private MeteoCalUser user;

    public Date getPickedDate() {
        return pickedDate;
    }

    public void setPickedDate(Date pickedDate) {
        this.pickedDate = pickedDate;
    }

    //metto qui questa variabile dato che voglio che venga ricordata across the session e userbean è sessionscoped
    private Date pickedDate;

    public MeteoCalUser getUser() {
        return user;
    }

    public void setUser(MeteoCalUser user) {
        this.user = user;
    }
}
