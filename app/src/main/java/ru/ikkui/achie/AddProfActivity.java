package ru.ikkui.achie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import ru.ikkui.achie.USM.USM;
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
        TextView profileName = findViewById(R.id.profNameFld);
        Intent intent = new Intent(this, MainMenuActivity.class);
        USM profile = new USM(profileName.getText().toString(), this);
        profile.create_isec("date");
        profile.create_ssec("object");
        profile.create_ssec("type");
        profile.create_ssec("measure");
        profile.create_isec("count");
        profile.create_ssec("photo");
        profile.to_file(this);
        intent.putExtra("profile", profile);
        startActivity(intent);
    }

}