package com.example.wthapp;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OnlineStrategy implements UpdateStrategy {

    private final String apiKey;
    private final String units; // "metric" or "imperial"

    public OnlineStrategy(String apiKey, String units) {
        this.apiKey = apiKey;
        this.units = units;
    }

    @Override
    public WeatherData fetchWeatherFor(String city) {
        try {
            String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city
                    + "&appid=" + apiKey + "&units=" + units + "&lang=ru";
            URL url = new URL(apiUrl);
            Scanner scanner = new Scanner(url.openStream());
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNext()) sb.append(scanner.nextLine());
            scanner.close();

            JSONObject json = new JSONObject(sb.toString());
            JSONArray list = json.getJSONArray("list");
            JSONObject main = list.getJSONObject(0).getJSONObject("main");

            double temp = main.getDouble("temp");
            double hum = main.getDouble("humidity");
            double press = main.getDouble("pressure");
            String desc = list.getJSONObject(0).getJSONArray("weather")
                    .getJSONObject(0).getString("description");

            List<HourlyData> hourly = new ArrayList<>();
            for (int i = 0; i < Math.min(12, list.length()); i++) {
                JSONObject item = list.getJSONObject(i);
                JSONObject mainItem = item.getJSONObject("main");
                String time = item.getString("dt_txt").substring(11, 16);
                double t = mainItem.getDouble("temp");
                String d = item.getJSONArray("weather").getJSONObject(0).getString("description");
                hourly.add(new HourlyData(time, t, d));
            }

            return new WeatherData(city, temp, hum, press, hourly, desc);

        } catch (Exception e) {
            Log.e("WeatherApp", "Error fetching weather: " + e.getMessage());
            return new WeatherData(city, 0, 0, 0, new ArrayList<>(), "Error fetching data");
        }
    }

    @Override
    public String name() { return "Online API"; }
}
