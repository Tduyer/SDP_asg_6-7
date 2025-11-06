package com.example.kmtn;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WeatherStation station;
    private TextView tvLog;
    private Spinner spinnerStrategy, spinnerObserverType;
    private EditText etTemp, etHum, etPress, etObserverName;
    private ListView listObservers;
    private ArrayAdapter<String> observersAdapter;
    private final List<WeatherObserver> observers = new ArrayList<>();
    private Handler schedulerHandler;
    private Runnable scheduledRunnable;
    private int scheduledIntervalSec = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLog = findViewById(R.id.tvLog);
        spinnerStrategy = findViewById(R.id.spinnerStrategy);
        spinnerObserverType = findViewById(R.id.spinnerObserverType);
        etTemp = findViewById(R.id.etTemp);
        etHum = findViewById(R.id.etHum);
        etPress = findViewById(R.id.etPress);
        etObserverName = findViewById(R.id.etObserverName);
        listObservers = findViewById(R.id.listObservers);

        ArrayAdapter<String> stratAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Manual", "Sensor", "Scheduled"});
        stratAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStrategy.setAdapter(stratAdapter);

        ArrayAdapter<String> obsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"UI", "Toast"});
        obsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerObserverType.setAdapter(obsAdapter);

        observersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listObservers.setAdapter(observersAdapter);

        station = new WeatherStation(new ManualInputStrategy(20.0, 50.0, 1013.0), tvLog);

        findViewById(R.id.btnUpdate).setOnClickListener(v -> onUpdateNow());
        findViewById(R.id.btnAddObserver).setOnClickListener(v -> onAddObserver());
        findViewById(R.id.btnStartScheduled).setOnClickListener(v -> onStartScheduled());
        findViewById(R.id.btnStopScheduled).setOnClickListener(v -> onStopScheduled());

        listObservers.setOnItemLongClickListener((parent, view, position, id) -> {
            if (position >= 0 && position < observers.size()) {
                WeatherObserver rem = observers.get(position);
                station.removeObserver(rem);
                observers.remove(position);
                observersAdapter.remove(observersAdapter.getItem(position));
                observersAdapter.notifyDataSetChanged();
                return true;
            }
            return false;
        });

        spinnerStrategy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String sel = (String) parent.getItemAtPosition(pos);
                switch (sel.toLowerCase()) {
                    case "manual":
                        station.setStrategy(new ManualInputStrategy(
                                parseDoubleOrDefault(etTemp.getText().toString(), 20.0),
                                parseDoubleOrDefault(etHum.getText().toString(), 50.0),
                                parseDoubleOrDefault(etPress.getText().toString(), 1013.0)
                        ));
                        break;
                    case "sensor":
                        station.setStrategy(new SensorUpdateStrategy());
                        break;
                    case "scheduled":
                        station.setStrategy(new ScheduledUpdateStrategy());
                        break;
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private double parseDoubleOrDefault(String s, double def) {
        try { return Double.parseDouble(s.trim()); }
        catch (Exception e) { return def; }
    }

    private void onUpdateNow() {
        if ("Manual".equalsIgnoreCase((String) spinnerStrategy.getSelectedItem())) {
            station.setStrategy(new ManualInputStrategy(
                    parseDoubleOrDefault(etTemp.getText().toString(), 20.0),
                    parseDoubleOrDefault(etHum.getText().toString(), 50.0),
                    parseDoubleOrDefault(etPress.getText().toString(), 1013.0)
            ));
        }
        station.updateWeather();
    }

    private void onAddObserver() {
        String type = (String) spinnerObserverType.getSelectedItem();
        String id = etObserverName.getText().toString().trim();
        if (id.isEmpty()) id = "Observer" + (observers.size() + 1);
        WeatherObserver o = ObserverFactory.create(type, id, this, tvLog);
        observers.add(o);
        station.addObserver(o);
        observersAdapter.add(o.getName());
        observersAdapter.notifyDataSetChanged();
        etObserverName.setText("");
    }

    private void onStartScheduled() {
        if (schedulerHandlerActive()) {
            Toast.makeText(this, "Scheduled already running", Toast.LENGTH_SHORT).show();
            return;
        }
        station.setStrategy(new ScheduledUpdateStrategy());
        schedulerHandler = new Handler();
        scheduledRunnable = new Runnable() {
            @Override
            public void run() {
                station.updateWeather();
                schedulerHandler.postDelayed(this, scheduledIntervalSec * 1000L);
            }
        };
        schedulerHandler.post(scheduledRunnable);
        Toast.makeText(this, "Scheduled updates started (" + scheduledIntervalSec + "s)", Toast.LENGTH_SHORT).show();
    }

    private boolean schedulerHandlerActive() {
        return schedulerHandler != null && scheduledRunnable != null;
    }

    private void onStopScheduled() {
        if (schedulerHandler != null && scheduledRunnable != null) {
            schedulerHandler.removeCallbacks(scheduledRunnable);
            scheduledRunnable = null;
            schedulerHandler = null;
            Toast.makeText(this, "Scheduled stopped", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No scheduled running", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onStopScheduled();
    }
}
