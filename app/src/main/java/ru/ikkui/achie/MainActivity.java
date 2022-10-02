package ru.ikkui.achie;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import ru.ikkui.achie.USM.USM;
import ru.ikkui.achie.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ArrayList<USM> profiles;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        profiles = USM.get_profiles(this);
        RecyclerView rec = findViewById(R.id.profiles_list);
        ProfileAdapter adapter = new ProfileAdapter(this, profiles);
        Toast.makeText(this, String.valueOf(profiles.size()), Toast.LENGTH_SHORT).show();
        rec.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addProfile(View view) {
        startActivity(new Intent(this, AddProfActivity.class));
    }

}