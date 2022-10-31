package ru.ikkui.achie;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.ikkui.achie.USSM.USM.USM;
import ru.ikkui.achie.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ArrayList<USM> profiles;
    ProfileAdapter adapter;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);



        profiles = USM.get_profiles(this);
        RecyclerView rec = findViewById(R.id.select_profiles);
        ProfileAdapter.OnProfileClickListener profileClickListener = new ProfileAdapter.OnProfileClickListener() {
            @Override
            public void onProfileClick(USM profile, int position) {
                Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
                adapter.notifyItemChanged(position);
            }
        };
        adapter = new ProfileAdapter(this, profiles, profileClickListener);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rec.setAdapter(adapter);
        rec.addItemDecoration(divider);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
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