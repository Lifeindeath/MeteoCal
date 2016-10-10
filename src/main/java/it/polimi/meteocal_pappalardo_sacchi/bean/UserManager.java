/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.bean;

import it.polimi.meteocal_pappalardo_sacchi.entity.MeteoCalUser;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 *
 *
 */
@Stateless
public class UserManager extends AbstractManager<MeteoCalUser> {
    @PersistenceContext(unitName = "authPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public boolean isUsernameUnique(String username) {
        List<String> usernameList = em.createNamedQuery("MeteoCalUser.findSameUsername")
                                      .setParameter("username", username)
                                      .getResultList();
        return usernameList.isEmpty();
    }

    public UserManager() {
        super(MeteoCalUser.class);
    }
    
}
