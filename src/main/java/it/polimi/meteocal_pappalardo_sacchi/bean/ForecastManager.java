/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.bean;


import it.polimi.meteocal_pappalardo_sacchi.entity.WeatherForecast;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;



/**
 *
 * @author Elena
 */
@Stateless
public class ForecastManager extends AbstractManager<WeatherForecast> {
    
    
    @PersistenceContext(unitName = "authPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ForecastManager() {
        super(WeatherForecast.class);
    }
}
