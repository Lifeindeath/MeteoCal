package it.polimi.meteocal_pappalardo_sacchi.bean;



import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Alessandro on 27/12/14.
 */
@Stateless
public class WeatherManager {

    @PersistenceContext(unitName = "authPU")
    private EntityManager em;
    
   
/*
    public String stub() {
        
        // declaring object of "OpenWeatherMap" class
        OpenWeatherMap owm = new OpenWeatherMap("");

        try {
            // getting current weather data for the "London" city
            dailyForecast = owm.dailyForecastByCityName("London", (byte)3);
        } catch (IOException ex) {
            Logger.getLogger(WeatherManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(WeatherManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        //printing city name from the retrieved data
        System.out.println("Valid: " + dailyForecast.isValid());

        // printing the max./min. temperature
       /* System.out.println("Temperature: " + cwd.getMainInstance().getMaxTemperature()
                            + "/" + cwd.getMainInstance().getMinTemperature() + "\'F");*/
    
        /*System.out.println("e0 ");
        weatherMap = new OpenWeatherMap(Units.METRIC, "778039ef9b1b2515f3e9f4d7d7207386");
        System.out.println("e1 ");
        w = weatherMap.currentWeatherByCityName("London");
        /*dailyForecast = weatherMap.dailyForecastByCityName("London", (byte)3);
        
        System.out.println("count: "+ dailyForecast.getForecastCount());
        System.out.println("response: "+ dailyForecast.getRawResponse());
        System.out.println("valid: "+ dailyForecast.isValid());
        //forecast = dailyForecast.getForecastInstance(1);
        if(dailyForecast.hasCityInstance())
            return "true";
        else
            return "false";
        
    }
    
    public void getWeatherByCityName() throws IOException, MalformedURLException, JSONException  {
        OpenWeatherMap openWeatherMap = new OpenWeatherMap("");
        DailyForecast dailyForecast = openWeatherMap.dailyForecastByCityName(cityName, (byte)15);
        //getWeatherConditionByDateFromForecastList(dailyForecast);
    }

    private void getWeatherConditionByDateFromForecastList(DailyForecast forecast) {
        WeatherCondition weatherCondition = new WeatherCondition();
        result = new String();
        for (DailyForecast.Forecast f : forecastList) {
            f.getDateTime();
        }
        return;
    }

    private boolean isForecastAvailable(java.util.Date date, DailyForecast dailyForecast) {
        //Faccio la differenza fra la data passata come parametro e quella attuale in ms, converto il risultato in giorni e lo confronto con la costante MIN DAYS TO FORECAST.
        if (TimeUnit.MILLISECONDS.toDays(date.getTime() - java.util.Calendar.getInstance().getTime().getTime()) > MIN_DAYS_TO_FORECAST) {
            return false;
        } else if (dailyForecast.)
            return true;
    }

    private DailyForecast.Forecast getForecastByDate(java.util.Date date, DailyForecast dailyForecast) {
        int numberOfDays = dailyForecast.getForecastCount();
        for (int i = 0; i < numberOfDays; i++) {
            if (dailyForecast.getForecastInstance(i).getDateTime().compareTo(date) == 0) {
                return dailyForecast.getForecastInstance(i);
            }
        }
        return null;
    }*//*

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private boolean isWeatherForecastAvailable(String cityName) {
        return false;
    }

    @Override
    protected EntityManager getEntityManager() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

}
