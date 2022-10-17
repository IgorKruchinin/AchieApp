package ru.ikkui.achie;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ru.ikkui.achie.USM.USM;
import ru.ikkui.achie.databinding.ActivityAddAchieBinding;

public class AddAchieActivity extends AppCompatActivity {

    USM profile;
    Date date;
    DatePickerDialog datePicker;
    DateFormat dateFormat;
    TextView achieDateFld;

    TextView achieObjectFld;
    List<String> achieObject;
    TextView achieTypeFld;
    List<String> achieType;
    TextView achieMeasureFld;
    List<String> achieMeasure;
    TextView achieCountFld;

    private String imagePath = "";
    private AppBarConfiguration appBarConfiguration;
    private ActivityAddAchieBinding binding;

    ActivityResultLauncher<String> getImagePath = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {
            File from =  new File(uri.getPath());
            int count;
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
                int length;
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

        achieDateFld = findViewById(R.id.achieDateFld);
        dateFormat = SimpleDateFormat.getDateInstance();

        achieDateFld.setInputType(InputType.TYPE_NULL);
        date = new Date();
        achieDateFld.setText(dateFormat.format(date));
        achieDateFld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                date.setTime(calendar.getTime().getTime());
                datePicker = new DatePickerDialog(AddAchieActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        achieDateFld.setText(dateFormat.format(date));
                    }
                }, year, month, day);
                datePicker.show();
            }
        });

        achieObjectFld = findViewById(R.id.achieObjectFld);
        achieObject  = profile.gets("object").getObjects_();
        achieObjectFld.setAutofillHints(achieObject.toArray(new String[achieObject.size()]));
        achieTypeFld = findViewById(R.id.achieTypeFld);
        achieType = profile.gets("type").getObjects_();
        achieTypeFld.setAutofillHints(achieType.toArray(new String[achieType.size()]));
        achieMeasureFld = findViewById(R.id.achieMeasureFld);
        achieMeasure = profile.gets("measure").getObjects_();
        achieMeasureFld.setAutofillHints(achieMeasure.toArray(new String[achieMeasure.size()]));
        achieCountFld = findViewById(R.id.achieCountFld);

    }

    public void add(View view) {
        Toast toast = Toast.makeText(this, profile.state, Toast.LENGTH_LONG);
        toast.show();
        if (!profile.opened()) {
            profile.create_isec("date");
            profile.create_ssec("object");
            profile.create_ssec("type");
            profile.create_ssec("measure");
            profile.create_isec("count");
            profile.create_ssec("photo");
        }


        profile.geti("date").add(date.getTime());
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