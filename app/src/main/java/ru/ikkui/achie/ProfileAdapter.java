package ru.ikkui.achie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.ikkui.achie.USM.USM;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private final List<USM> profiles;
    ProfileAdapter(Context context, List<USM> listProfiles) {
        this.profiles = listProfiles;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View view = inflater.inflate(R.layout.list_achies, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ProfileAdapter.ViewHolder holder, int position) {
        USM profile = profiles.get(position);
        holder.name.setText(profile.get_name());
    }
    @Override
    public int getItemCount() {
        return profiles.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.item_name);
        }
    }
}
