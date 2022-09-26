package ru.ikkui.achie;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Vector;

import ru.ikkui.achie.databinding.ActivityMainMenuBinding;

import ru.ikkui.achie.USM.*;

public class MainMenuActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainMenuBinding binding;
    private Button addAchieBtn;
    //private USM profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        /*List<String> achiesStrings = new Vector<String>();
        for (int i = 0; i < profile.size(); ++i) {
            achiesStrings.add(profile.gets("date").get(i) + " " + profile.gets("object").get(i));
        }
        Spinner achiesList = findViewById(R.id.listAchies);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, achiesStrings);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        achiesList.setAdapter(arrayAdapter);*/
    }

    public void addAchie(View v) {
        PopupMenu menu = new PopupMenu(MainMenuActivity.this, v);
        menu.getMenuInflater().inflate(R.menu.popup_menu, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.addAchie:
                        Intent intent = new Intent(MainMenuActivity.this, AddAchieActivity.class);
                        //intent.putExtra("profile", profile);
                        startActivity(intent);
                        break;
                    case R.id.addToPlan:
                        Toast toast = Toast.makeText(MainMenuActivity.this, "This content not realised yet", Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                }
                return true;
            }
        });
        menu.show();
    }

}