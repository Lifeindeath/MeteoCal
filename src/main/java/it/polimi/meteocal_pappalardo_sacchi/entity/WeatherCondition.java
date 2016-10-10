/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Embeddable;

/**
 *
 * @author Alessandro
 */
@Embeddable
public class WeatherCondition implements Serializable {
    
   private float windStrength;
   private List<PrecipitationType> precipitationType;
   private float temperature;

    public float getWindStrength() {
        return windStrength;
    }

    public void setWindStrength(float windStrength) {
        this.windStrength = windStrength;
    }

    public List<PrecipitationType> getPrecipitationType() {
        return precipitationType;
    }

    public void setPrecipitationType(List<PrecipitationType> precipitationType) {
        this.precipitationType = precipitationType;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
   
   
}
