package ru.ikkui.achie;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

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

import ru.ikkui.achie.USSM.USM.USM;
import ru.ikkui.achie.databinding.ActivityAchieEditBinding;

public class AchieEditActivity extends AppCompatActivity {

    USM profile;
    int achieIndex;

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
    private ActivityAchieEditBinding binding;

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
                Toast.makeText(AchieEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAchieEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Bundle arguments = getIntent().getExtras();
        profile = (USM)arguments.get("profile");
        achieIndex = arguments.getInt("index");

        achieDateFld = findViewById(R.id.achieDateFld);
        dateFormat = SimpleDateFormat.getDateInstance();

        achieDateFld.setInputType(InputType.TYPE_NULL);
        date = new Date(profile.geti("date").get(achieIndex));
        achieDateFld.setText(dateFormat.format(date));
        achieDateFld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                date.setTime(calendar.getTime().getTime());
                datePicker = new DatePickerDialog(AchieEditActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        achieObjectFld.setText(profile.gets("object").get(achieIndex));
        achieObjectFld.setAutofillHints(achieObject.toArray(new String[achieObject.size()]));
        achieTypeFld = findViewById(R.id.achieTypeFld);
        achieTypeFld.setText(profile.gets("type").get(achieIndex));
        achieType = profile.gets("type").getObjects_();
        achieTypeFld.setAutofillHints(achieType.toArray(new String[achieType.size()]));
        achieMeasureFld = findViewById(R.id.achieMeasureFld);
        achieMeasureFld.setText(profile.gets("measure").get(achieIndex));
        achieMeasure = profile.gets("measure").getObjects_();
        achieMeasureFld.setAutofillHints(achieMeasure.toArray(new String[achieMeasure.size()]));
        achieCountFld = findViewById(R.id.achieCountFld);
        achieCountFld.setText(String.valueOf(profile.geti("count").get(achieIndex)));

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


        profile.geti("date").edit(achieIndex, date.getTime());
        profile.gets("object").edit(achieIndex, achieObjectFld.getText().toString());
        profile.gets("type").edit(achieIndex, achieTypeFld.getText().toString());
        profile.gets("measure").edit(achieIndex, achieMeasureFld.getText().toString());
        profile.geti("count").edit(achieIndex, Integer.parseInt(achieCountFld.getText().toString()));
        try {
            profile.gets("photo").edit(achieIndex, imagePath);
        } catch (NullPointerException e) {
            profile.create_ssec("photo");
            profile.gets("photo").add(imagePath);
        }
        profile.to_file(this);
        finish();
    }

    public void loadImage(View view) {
        File oldFile = new File(getExternalFilesDir(null), "profiles" + File.separator + "res" + File.separator + profile.get_name() + File.separator + profile.gets("photo").get(achieIndex));
        oldFile.delete();
        getImagePath.launch("image/*");
    }
}