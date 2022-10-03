package ru.ikkui.achie;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ru.ikkui.achie.USM.USM;
import ru.ikkui.achie.databinding.ActivityViewAchieBinding;

public class ViewAchieActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityViewAchieBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewAchieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Bundle arguments = getIntent().getExtras();

        USM profile = (USM)arguments.get("profile");
        int index = arguments.getInt("index");

        TextView viewDate = findViewById(R.id.viewDate);
        TextView viewObject = findViewById(R.id.viewObject);
        TextView viewType = findViewById(R.id.viewType);
        TextView viewCount = findViewById(R.id.viewCount);

        if (index < profile.gets("date").size()) {
            viewDate.setText(profile.gets("date").get(index));
            viewObject.setText(profile.gets("object").get(index));
            viewType.setText(profile.gets("type").get(index));
            viewCount.setText(String.valueOf(profile.geti("count").get(index)) + " " + profile.gets("measure").get(index));
        }

    }

}