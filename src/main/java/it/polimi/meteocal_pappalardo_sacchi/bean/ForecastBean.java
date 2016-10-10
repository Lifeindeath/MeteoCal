/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal_pappalardo_sacchi.bean;

import it.polimi.meteocal_pappalardo_sacchi.entity.Event;
import it.polimi.meteocal_pappalardo_sacchi.entity.WeatherCondition;
import it.polimi.meteocal_pappalardo_sacchi.entity.WeatherForecast;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import net.aksingh.java.api.owm.DailyForecastData;
import net.aksingh.java.api.owm.DailyForecastData.Forecast;
import net.aksingh.java.api.owm.OpenWeatherMap;
import org.json.JSONException;


/**
 *
 * @author Elena
 */
@Named
@RequestScoped
public class ForecastBean implements Serializable {

    @Inject
    ForecastManager forecastManager;
    @Inject 
    WeatherBean weatherBean;

    private WeatherForecast weatherForecast;
    private List<WeatherForecast> weatherForecastList;
    
    private static final long MIN_DAYS_TO_FORECAST = 15;

    public ForecastBean(){
    }

    public WeatherForecast getWeatherForecast() {
        if (weatherForecast==null) {
            weatherForecast = new WeatherForecast();
        }
        return weatherForecast;
        
    }

    public void setWeatherForecast(WeatherForecast weatherForecast) {
        this.weatherForecast = weatherForecast;
    }
    
    public List<WeatherForecast> getWeatherForecastList() {
        if (weatherForecastList==null) {
            weatherForecastList = new ArrayList<WeatherForecast>();
        }
        return weatherForecastList;
    }

    public void setWeatherForecastList(List<WeatherForecast> weatherForecastList) {
        this.weatherForecastList = weatherForecastList;
    }
    
    public boolean retrieveMultipleForecast(String cityName, java.sql.Date startingDate, java.sql.Date endingDate){
        
        OpenWeatherMap owm = new OpenWeatherMap(OpenWeatherMap.OWM_URL.PARAMETER_UNITS_VALUE_METRIC,"");
        DailyForecastData dailyForecast; 

        try {
            byte days = daysUntilDate(endingDate);
            dailyForecast = owm.dailyForecastByCityName(cityName, days);
            
            for(Forecast df : dailyForecast.getForecast_List())
                if(!df.getDateTime().before(startingDate)){
                    compose(df);
                    getWeatherForecastList().add(getWeatherForecast());
                    setWeatherForecast(new WeatherForecast());
                }
            return true;
        } catch (IOException ex) {
            Logger.getLogger(WeatherManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (JSONException ex) {
            Logger.getLogger(WeatherManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }                
    }
    /*
    public boolean retrieveForecast(String cityName, java.sql.Date date) {
        //Da mettere poi in forecastmanager per non reinvocarli ogni volta
        OpenWeatherMap owm = new OpenWeatherMap(OpenWeatherMap.OWM_URL.PARAMETER_UNITS_VALUE_METRIC,"");
        DailyForecastData dailyForecast;        
        
        try {
            byte days = daysUntilDate(date);
            dailyForecast = owm.dailyForecastByCityName(cityName, days); 
            //NOTE TO SELF: WeahterName is like rain, snow, clear; WeatherDescription is like light/heavy rain, light/heavy snow, Sky is clear
            System.out.println(dailyForecast.getForecast_List().get(days-1).getWeather_List().get(0).getWeatherName());
            System.out.println(dailyForecast.getForecast_List().get(days-1).getWeather_List().get(0).getWeatherDescription());
            compose(dailyForecast.getForecast_List().get(days-1));
            //forecastManager.create(weatherForecast); (no need, cascade)
            /*System.out.println("date "+dailyForecast.getForecast_List().get(days-1).getDateTime());
            System.out.println("temp "+dailyForecast.getForecast_List().get(days-1).getTemperature_Object().getDayTemperature());
            System.out.println("wind "+dailyForecast.getForecast_List().get(days-1).getWindDegree());
            System.out.println("weather "+dailyForecast.getForecast_List().get(days-1).getWeather_List().get(0).getWeatherName());
            
            return true;
        } catch (IOException ex) {
            Logger.getLogger(WeatherManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (JSONException ex) {
            Logger.getLogger(WeatherManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }     
      
    }*/
    
    private Byte daysUntilDate(java.sql.Date date){
        ZoneId zone = ZoneId.of("Europe/Paris");
        LocalDate now = LocalDate.now(zone);
        LocalDate eventDate = date.toLocalDate();
                 
        Period period = now.until(eventDate);
        byte days = (byte)(period.getDays() + 1);
       
        if (days>MIN_DAYS_TO_FORECAST)
            return (byte)0;
        else
            System.out.println(""+days);
        
        return (byte)days;
    }

    private void compose(Forecast forecast) {
        System.out.println("forecast" + forecast.toString());
        if (forecast.hasDateTime()){
        getWeatherForecast().setStartingDate(new java.sql.Date (forecast.getDateTime().getTime()));
        getWeatherForecast().setStartingTime(new java.sql.Time(forecast.getDateTime().getTime()));
        } else
            System.out.println("no datetime");            
            
        WeatherCondition wc = weatherBean.createFromForecast(forecast);
        getWeatherForecast().setWeatherCondition(wc);
    }
}
