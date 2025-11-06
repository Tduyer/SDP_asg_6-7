package com.example.wthapp;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.VH> {
    private final List<HourlyData> items;

    public HourAdapter(List<HourlyData> items) { this.items = items; }

    public static class VH extends RecyclerView.ViewHolder {
        TextView tvHour, tvTemp, tvDesc;
        VH(View v) {
            super(v);
            tvHour = v.findViewById(R.id.tvHour);
            tvTemp = v.findViewById(R.id.tvTemp);
            tvDesc = v.findViewById(R.id.tvDesc);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hour, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        HourlyData h = items.get(position);
        holder.tvHour.setText(h.getHour());
        holder.tvTemp.setText(String.format(Locale.getDefault(), "%.1f°", h.getTemp()));
        holder.tvDesc.setText(h.getDesc());
    }

    @Override
    public int getItemCount() { return items.size(); }

    public void setHours(List<HourlyData> hours) {
        items.clear();
        items.addAll(hours);
        notifyDataSetChanged();
    }
}
