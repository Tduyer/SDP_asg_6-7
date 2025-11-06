package com.example.wthapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private TextView tvCity, tvTemperature, tvDescription, tvDateTime;
    private ImageView ivWeatherIcon;
    private RecyclerView rvHours;
    private Button btnRefresh, btnSearch;
    private EditText etCity;
    private Spinner spUnits;

    private HourAdapter hourAdapter;
    private final String apiKey = "828211e914bcfa712911bc7e32942196";
    private String selectedUnits = "metric";
    private final String defaultCity = "Greenwich";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCity = findViewById(R.id.tvCity);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvDescription = findViewById(R.id.tvDescription);
        tvDateTime = findViewById(R.id.tvDateTime);
        ivWeatherIcon = findViewById(R.id.ivWeatherIcon);
        rvHours = findViewById(R.id.rvHours);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnSearch = findViewById(R.id.btnSearch);
        etCity = findViewById(R.id.etCity);
        spUnits = findViewById(R.id.spUnits);

        rvHours.setLayoutManager(new LinearLayoutManager(this));
        hourAdapter = new HourAdapter(new ArrayList<>());
        rvHours.setAdapter(hourAdapter);

        spUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUnits = (position == 0) ? "metric" : "imperial";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnRefresh.setOnClickListener(v -> fetchWeather(tvCity.getText().toString()));

        btnSearch.setOnClickListener(v -> {
            String city = etCity.getText().toString().trim();
            if (!city.isEmpty()) {
                fetchWeather(city);
            } else {
                Toast.makeText(MainActivity.this, "Please enter a city", Toast.LENGTH_SHORT).show();
            }
        });

        fetchWeather(defaultCity);
    }

    private void fetchWeather(String city) {
        executor.execute(() -> {
            WeatherData data = null;
            try {
                String urlStr = "https://api.openweathermap.org/data/2.5/forecast?q="
                        + city + "&appid=" + apiKey + "&units=" + selectedUnits + "&lang=en";
                URL url = new URL(urlStr);
                Scanner sc = new Scanner(url.openStream());
                StringBuilder sb = new StringBuilder();
                while (sc.hasNext()) sb.append(sc.nextLine());
                sc.close();

                JSONObject json = new JSONObject(sb.toString());
                JSONArray list = json.getJSONArray("list");
                JSONObject main = list.getJSONObject(0).getJSONObject("main");

                double temp = main.getDouble("temp");
                String desc = list.getJSONObject(0)
                        .getJSONArray("weather").getJSONObject(0).getString("description");

                List<HourlyData> hours = new ArrayList<>();
                for (int i = 0; i < Math.min(12, list.length()); i++) {
                    JSONObject item = list.getJSONObject(i);
                    JSONObject mainItem = item.getJSONObject("main");
                    String time = item.getString("dt_txt").substring(11, 16);
                    double t = mainItem.getDouble("temp");
                    String d = item.getJSONArray("weather")
                            .getJSONObject(0).getString("description");
                    hours.add(new HourlyData(time, t, d));
                }

                data = new WeatherData(city, temp, 0, 0, hours, desc);
            } catch (Exception e) {
                e.printStackTrace();
            }

            WeatherData finalData = data;
            handler.post(() -> {
                if (finalData == null) {
                    Toast.makeText(MainActivity.this, "Error loading weather", Toast.LENGTH_SHORT).show();
                } else {
                    updateUI(finalData);
                }
            });
        });
    }

    private void updateUI(WeatherData data) {
        tvCity.setText(data.getCity());
        String unitSymbol = selectedUnits.equals("metric") ? "°C" : "°F";
        tvTemperature.setText(String.format(Locale.getDefault(), "%.0f%s", data.getTemperature(), unitSymbol));
        tvDescription.setText(capitalizeFirst(data.getDescription()));
        tvDateTime.setText(new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(new Date()));

        String desc = data.getDescription().toLowerCase(Locale.ROOT);
        if (desc.contains("rain")) {
            ivWeatherIcon.setImageResource(android.R.drawable.ic_menu_compass);
        } else if (desc.contains("cloud")) {
            ivWeatherIcon.setImageResource(android.R.drawable.ic_menu_gallery);
        } else if (desc.contains("snow")) {
            ivWeatherIcon.setImageResource(android.R.drawable.ic_menu_mylocation);
        } else {
            ivWeatherIcon.setImageResource(android.R.drawable.ic_menu_day);
        }

        hourAdapter.setHours(data.getHourly());
        ivWeatherIcon.setVisibility(View.GONE);
    }

    private String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) return "";
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}
