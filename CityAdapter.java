package com.example.wthapp;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.VH> {
    private final List<String> items;
    private final WeatherStation station;
    private final OnSelect listener;

    public interface OnSelect {
        void onCitySelected(String city);
    }

    public CityAdapter(List<String> items, WeatherStation station, OnSelect listener) {
        this.items = items;
        this.station = station;
        this.listener = listener;
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvSummary;
        VH(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvCityName);
            tvSummary = v.findViewById(R.id.tvCitySummary);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        String city = items.get(position);
        holder.tvName.setText(city);
        WeatherData d = station.getData(city);
        holder.tvSummary.setText(d != null ? "Now: " + d.getTemperature() + "°" : "No data");
        holder.itemView.setOnClickListener(v -> listener.onCitySelected(city));
    }

    @Override
    public int getItemCount() { return items.size(); }

    public void add(String city) {
        items.add(city);
        notifyItemInserted(items.size() - 1);
    }
}
