package ru.ikkui.achie;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ru.ikkui.achie.databinding.ActivityAddProfBinding;

public class AddProfActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAddProfBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddProfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
    }

    public void createProfile(View view) {
        // Creating USM profile
        startActivity(new Intent(this, MainMenuActivity.class));
    }

}