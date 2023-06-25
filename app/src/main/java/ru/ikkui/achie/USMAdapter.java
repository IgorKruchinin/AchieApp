package ru.ikkui.achie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import ru.ikkui.achie.USSM.USM.USM;

public class USMAdapter extends RecyclerView.Adapter<USMAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private USM profile;
    private List<Long> dates;
    private List<String> objects;
    private List<Long> counts;

    interface OnAchieClickListener {
        void onAchieClick(USM profile, int position);
        void onAchieLongClick(View view, USM profile, int position);
    }



    private final USMAdapter.OnAchieClickListener onClickListener;


    USMAdapter(Context context, USM profile, OnAchieClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.profile = profile;
        this.dates = this.profile.geti("date").getObjects_();
        this.objects = this.profile.gets("object").getObjects_();
        this.counts = this.profile.geti("count").getObjects_();
        this.inflater = LayoutInflater.from(context);
    }
    public void reset(USM anotherProfile) {
        profile = anotherProfile;
        dates = this.profile.geti("date").getObjects_();
        objects = this.profile.gets("object").getObjects_();
        counts = this.profile.geti("count").getObjects_();
        notifyDataSetChanged();
    }
    @Override
    public USMAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View view = inflater.inflate(R.layout.list_achies, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(USMAdapter.ViewHolder holder, int position) {
        if (dates.size() > 0) {
            java.util.Date date = new java.util.Date(dates.get(position));
            DateFormat dateFormat = SimpleDateFormat.getDateInstance();
            holder.date.setText(dateFormat.format(date));
            holder.object.setText(objects.get(position));
            long count = counts.get(position);
            if (count >= 0) {
                holder.count.setText(String.valueOf(count));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onAchieClick(profile, holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onClickListener.onAchieLongClick(holder.itemView, profile, holder.getAdapterPosition());
                    return true;
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
