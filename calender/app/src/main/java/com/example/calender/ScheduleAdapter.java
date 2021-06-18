package com.example.calender;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private final List<Schedule> data;
    private final Consumer<Schedule> onScheduleClickListener;

    public ScheduleAdapter(List<Schedule> data, Consumer<Schedule> onScheduleClickListener) {
        this.data = data;
        this.onScheduleClickListener = onScheduleClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule s = data.get(position);

        holder.tvTitle.setText(s.title);
        holder.tvDate.setText(getDateString(s.year, s.month, s.date));
        holder.tvTime.setText(getTimeString(s.hour, s.minute));

        holder.itemView.setOnClickListener(v -> onScheduleClickListener.accept(s));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static String getDateString(int year, int month, int date) {
        return String.format(Locale.getDefault(), "%d-%02d-%02d", year, month, date);
    }

    private static String getTimeString(int hour, int minute) {
        return String.format(Locale.getDefault(), "%d:%02d", hour, minute);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvTitle;
        public final TextView tvDate;
        public final TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
