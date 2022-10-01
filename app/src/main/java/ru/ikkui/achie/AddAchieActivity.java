package ru.ikkui.achie;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ru.ikkui.achie.USM.USM;
import ru.ikkui.achie.databinding.ActivityAddAchieBinding;

public class AddAchieActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAddAchieBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddAchieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

    }

    public void add(View view) {
        Bundle arguments = getIntent().getExtras();
        USM profile = new USM("profile", this);
        Toast toast = Toast.makeText(this, profile.state, Toast.LENGTH_LONG);
        toast.show();
        if (!profile.opened()) {
            profile.create_ssec("date");
            profile.create_ssec("object");
            profile.create_ssec("type");
            profile.create_ssec("measure");
            profile.create_isec("count");
            toast = Toast.makeText(this, "Sections created", Toast.LENGTH_SHORT);
            toast.show();
        }
        TextView achieDateFld = findViewById(R.id.achieDateFld);
        TextView achieObjectFld = findViewById(R.id.achieObjectFld);
        TextView achieTypeFld = findViewById(R.id.achieTypeFld);
        TextView achieMeasureFld = findViewById(R.id.achieMeasureFld);
        TextView achieCountFld = findViewById(R.id.achieCountFld);
        toast = Toast.makeText(this, "TextViews found: " + achieDateFld.getText(), Toast.LENGTH_SHORT);
        toast.show();
        profile.gets("date").add(achieDateFld.getText().toString());
        profile.gets("object").add(achieObjectFld.getText().toString());
        profile.gets("type").add(achieTypeFld.getText().toString());
        profile.gets("measure").add(achieMeasureFld.getText().toString());
        profile.geti("count").add(Integer.parseInt(achieCountFld.getText().toString()));
        toast = Toast.makeText(this, "Information added", Toast.LENGTH_SHORT);
        toast.show();
        profile.to_file(this);
        toast = Toast.makeText(this, "Completed", Toast.LENGTH_SHORT);
        toast.show();
        //finish();
    }

}