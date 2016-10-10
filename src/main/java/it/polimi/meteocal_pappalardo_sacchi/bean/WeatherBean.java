/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.bean;

import it.polimi.meteocal_pappalardo_sacchi.entity.PrecipitationType;
import it.polimi.meteocal_pappalardo_sacchi.entity.WeatherCondition;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import net.aksingh.java.api.owm.AbstractWeatherData.Weather;
import net.aksingh.java.api.owm.DailyForecastData.Forecast;


/**
 *
 * @author Elena
 */
@Named
@ViewScoped
public class WeatherBean implements Serializable{
    
    private WeatherCondition weatherCondition;
    private List<PrecipitationType> conditions;
    
     @PostConstruct
    public void init() {
        conditions = new ArrayList<PrecipitationType>();
        conditions.add(PrecipitationType.CLOUDY);
        conditions.add(PrecipitationType.SNOW);        
        conditions.add(PrecipitationType.MID_RAIN);        
    }
    
    public List<PrecipitationType> getConditions() {
        return conditions;
    }
    
    public WeatherCondition getWeatherCondition() {
        if (weatherCondition==null) {
            weatherCondition = new WeatherCondition();
        }
        return weatherCondition;
    }

    public void setEvent(WeatherCondition weatherCondition) {
        this.weatherCondition = weatherCondition;
    }
    
    
    public WeatherCondition createFromForecast(Forecast forecast){

        WeatherCondition weatherCond = new WeatherCondition();
        //In Celsius 
        weatherCond.setTemperature(forecast.getTemperature_Object().getDayTemperature());
        //Wind speed in m/s -> fuzzify!
        weatherCond.setWindStrength(forecast.getWindSpeed());
        //Converts weather condition from api to PrecipitationType
        List<PrecipitationType> prec = new ArrayList<PrecipitationType>();
        prec.add(convertWeatherName(forecast.getWeather_List().get(0)));
        weatherCond.setPrecipitationType(prec);
        
        System.out.println("weather name: " + forecast.getWeather_List().get(0).getWeatherName());        
        return weatherCond;
    }

    private PrecipitationType convertWeatherName(Weather weather) {
        String weatherName = weather.getWeatherName();
        switch(weatherName){
            case "Clear":
                return PrecipitationType.SUNNY;
            case "Clouds":
                return PrecipitationType.CLOUDY;
            case "Rain":
                return PrecipitationType.MID_RAIN;
            case "Snow":
                return PrecipitationType.SNOW;   
            default:
                return PrecipitationType.SUNNY;
        }
    }
}
