package openweather.openweather;


import java.util.HashMap;
import java.util.List;

public class OpenWeatherProvider {
    private int weather_id;
    private Double weather_temp;
    private String weather_description;
    private int weather_humidity;
    private String weather_time;
    private HashMap<String, List<String>> map = new HashMap<String, List<String>>();

    public OpenWeatherProvider() {

    }

    public OpenWeatherProvider(int weather_id, Double weather_temp, String weather_description, int weather_humidity, String weather_time) {

        this.weather_id=weather_id;
        this.weather_temp = weather_temp;
        this.weather_description = weather_description;
        this.weather_humidity = weather_humidity;
        this.weather_time = weather_time;

    }

    public void set_openweatherprovider(HashMap<String, List<String>> map) {
        this.map = map;
    }

    public HashMap<String, List<String>> get_openweaherprovider() {
        return this.map;
    }

    public void setweather_id(int id) {
        this.weather_id = id;
    }

    public int getweather_id() {
        return this.weather_id;
    }


    public void setweather_temp(Double temp) {
        this.weather_temp = temp;
    }

    public Double getweather_temp() {
        return this.weather_temp;
    }

    public void setweather_description(String tempmax) {
        this.weather_description = tempmax;
    }

    public String getweather_description() {
        return this.weather_description;
    }

    public void setweather_humidity(int tempmin) {
        this.weather_humidity = tempmin;
    }

    public int getweather_humidity() {
        return this.weather_humidity;
    }

    public void set_weather_windtime(String time) {
        this.weather_time = time;
    }

    public String getweather_time() {
        return this.weather_time;
    }

}
