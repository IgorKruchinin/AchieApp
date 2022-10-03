package ru.ikkui.achie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.ikkui.achie.USM.USM;

public class USMAdapter extends RecyclerView.Adapter<USMAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private final USM profile;
    private final List<String> dates;
    private final List<String> objects;
    private final List<String> measures;
    private final List<Integer> counts;

    interface OnAchieClickListener {
        void onAchieClick(USM profile, int position);
    }

    private final USMAdapter.OnAchieClickListener onClickListener;


    USMAdapter(Context context, USM profile, OnAchieClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.profile = profile;
        this.dates = this.profile.gets("date").getObjects_();
        this.objects = this.profile.gets("object").getObjects_();
        this.measures = this.profile.gets("measure").getObjects_();
        this.counts = this.profile.geti("count").getObjects_();
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public USMAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View view = inflater.inflate(R.layout.list_achies, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(USMAdapter.ViewHolder holder, int position) {
        if (dates.size() > 0) {
            holder.date.setText(dates.get(position));
            holder.object.setText(objects.get(position));
            holder.count.setText(String.valueOf(counts.get(position)) +  " " + measures.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onAchieClick(profile, holder.getAdapterPosition());
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return dates.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView date;
        final TextView object;
        final TextView count;
        ViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.item_achie_date);
            object = view.findViewById(R.id.item_achie_object);
            count = view.findViewById(R.id.item_achie_count);
        }
    }
}
