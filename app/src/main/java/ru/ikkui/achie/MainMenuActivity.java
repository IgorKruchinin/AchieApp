package ru.ikkui.achie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.ikkui.achie.USM.IntSection;
import ru.ikkui.achie.USM.Section;
import ru.ikkui.achie.USM.StringSection;
import ru.ikkui.achie.USM.USM;
import ru.ikkui.achie.databinding.ActivityMainMenuBinding;

public class MainMenuActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainMenuBinding binding;
    private Button addAchieBtn;
    private USM profile;
    private USMAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Bundle arguments = getIntent().getExtras();

        profile = (USM) arguments.get("profile");

        if (profile != null) {

            USMAdapter.OnAchieClickListener achieClickListener = new USMAdapter.OnAchieClickListener() {
                @Override
                public void onAchieClick(USM profile, int position) {
                    Intent intent = new Intent(MainMenuActivity.this, ViewAchieActivity.class);
                    intent.putExtra("profile", profile);
                    intent.putExtra("index", position);
                    startActivity(intent);
                }

                @Override
                public void onAchieLongClick(View view, USM profile, int position) {
                    PopupMenu menu = new PopupMenu(MainMenuActivity.this, view);
                    menu.getMenuInflater().inflate(R.menu.achie_popup_menu, menu.getMenu());
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.edit_achie:
                                    Intent editIntent = new Intent(MainMenuActivity.this, AchieEditActivity.class);
                                    editIntent.putExtra("profile", profile);
                                    editIntent.putExtra("index", position);
                                    startActivity(editIntent);
                                    break;
                                case R.id.deleteAchie:
                                    profile.geti("date").remove(position);
                                    profile.gets("object").remove(position);
                                    profile.gets("type").remove(position);
                                    profile.gets("measure").remove(position);
                                    profile.geti("count").remove(position);
                                    profile.gets("photo").remove(position);
                                    profile.to_file(MainMenuActivity.this);
                                    adapter.notifyDataSetChanged();
                                    break;
                            }
                            return true;
                        }
                    });
                    menu.show();
                }
            };

            RecyclerView recyclerView = findViewById(R.id.achies_list);

            adapter = new USMAdapter(this, profile, achieClickListener);

            DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(divider);

        }


        /*List<String> achiesStrings = new Vector<String>();
        for (int i = 0; i < profile.size(); ++i) {
            achiesStrings.add(profile.geti("date").get(i) + " " + profile.gets("object").get(i));
        }
        Spinner achiesList = findViewById(R.id.listAchies);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, achiesStrings);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        achiesList.setAdapter(arrayAdapter);*/
    }

    @Override
    public void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
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
                        intent.putExtra("profile", profile);
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

    public void export(View view) {
        to_csv();
    }

    public void to_csv() {
        File csv = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), profile.get_name() + ".csv");
        try {
            if (!csv.exists()) {
                csv.createNewFile();
            }
            BufferedWriter csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv, false)));
            int size = 0;
            for (Section s: profile.getAll()) {
                size = s.size();
                csvWriter.write(s.get_name());
                csvWriter.write(",");
                //if (s.size() < min_size) {
                    //min_size = s.size();
                //}
            }
            csvWriter.write("\n");
            for (int index = 0; index < size; ++index) {
                for (Section s : profile.getAll()) {
                    if (s instanceof StringSection) {
                        csvWriter.write(((StringSection)s).get(index));
                    } if (s instanceof IntSection) {
                        csvWriter.write(String.valueOf(((IntSection)s).get(index)));
                    }
                    csvWriter.write(",");
                }
                csvWriter.write("\n");
            }
            csvWriter.flush();
            csvWriter.close();
            Toast.makeText(this, csv.getPath().toString(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

}