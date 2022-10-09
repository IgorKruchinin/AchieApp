package ru.ikkui.achie;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.ikkui.achie.USM.USM;
import ru.ikkui.achie.databinding.ActivityAddAchieBinding;

public class AddAchieActivity extends AppCompatActivity {

    USM profile;
    private String imagePath = "";
    private AppBarConfiguration appBarConfiguration;
    private ActivityAddAchieBinding binding;

    ActivityResultLauncher<String> getImagePath = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {
            File from =  new File(uri.getPath());
            int count = 0;
            try {
                count = profile.gets("photo").size();
            } catch (NullPointerException e) {
                profile.create_ssec("photo");
                count = profile.gets("photo").size();
            }

            File dir = new File(getExternalFilesDir(null), "profiles" + File.separator + "res" + File.separator + profile.get_name());
            File to = new File(dir, String.valueOf(count));
            try {
                dir.mkdirs();
                to.createNewFile();
                InputStream input = getContentResolver().openInputStream(uri);
                OutputStream output = new FileOutputStream(to);

                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                imagePath = String.valueOf(profile.gets("photo").size());
            } catch (IOException e) {
                Toast.makeText(AddAchieActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddAchieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Bundle arguments = getIntent().getExtras();
        profile = (USM)arguments.get("profile");

    }

    public void add(View view) {
        Toast toast = Toast.makeText(this, profile.state, Toast.LENGTH_LONG);
        toast.show();
        if (!profile.opened()) {
            profile.create_ssec("date");
            profile.create_ssec("object");
            profile.create_ssec("type");
            profile.create_ssec("measure");
            profile.create_isec("count");
            profile.create_ssec("photo");
        }
        TextView achieDateFld = findViewById(R.id.achieDateFld);
        TextView achieObjectFld = findViewById(R.id.achieObjectFld);
        TextView achieTypeFld = findViewById(R.id.achieTypeFld);
        TextView achieMeasureFld = findViewById(R.id.achieMeasureFld);
        TextView achieCountFld = findViewById(R.id.achieCountFld);
        profile.gets("date").add(achieDateFld.getText().toString());
        profile.gets("object").add(achieObjectFld.getText().toString());
        profile.gets("type").add(achieTypeFld.getText().toString());
        profile.gets("measure").add(achieMeasureFld.getText().toString());
        profile.geti("count").add(Integer.parseInt(achieCountFld.getText().toString()));
        try {
            profile.gets("photo").add(imagePath);
        } catch (NullPointerException e) {
            profile.create_ssec("photo");
            profile.gets("photo").add(imagePath);
        }
        profile.to_file(this);
        finish();
    }

    public void loadImage(View view) {
        getImagePath.launch("image/*");
    }

}