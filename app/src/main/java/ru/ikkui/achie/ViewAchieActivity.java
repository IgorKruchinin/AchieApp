package ru.ikkui.achie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLConnection;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import ru.ikkui.achie.USM.USM;
import ru.ikkui.achie.databinding.ActivityViewAchieBinding;

public class ViewAchieActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityViewAchieBinding binding;
    Uri image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewAchieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Bundle arguments = getIntent().getExtras();

        USM profile = (USM)arguments.get("profile");
        int index = arguments.getInt("index");


        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        TextView viewDate = findViewById(R.id.viewDate);
        TextView viewObject = findViewById(R.id.viewObject);
        TextView viewType = findViewById(R.id.viewType);
        TextView viewCount = findViewById(R.id.viewCount);
        ImageView viewPhoto = findViewById(R.id.viewPhoto);

        FloatingActionButton shareBtn = findViewById(R.id.shareAchieBtn);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    File archive = profile.to_one_archive(ViewAchieActivity.this, profile.get_name(), "ach", index, profile.gets("photo").get(index));
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("application/x-zip-compressed");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(ViewAchieActivity.this, BuildConfig.APPLICATION_ID, archive));
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "Send File"));
                } catch (IOException ex) {
                    Toast.makeText(ViewAchieActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();;
                }
            }
        });

        DateFormat dateFormat = SimpleDateFormat.getDateInstance();

        if (index < profile.geti("date").size()) {
            Date date = new Date(profile.geti("date").get(index));
            viewDate.setText(dateFormat.format(date));
            viewObject.setText(profile.gets("object").get(index));
            viewType.setText(profile.gets("type").get(index));
            if (profile.geti("count").get(index) >= 0) {
                viewCount.setText(String.valueOf(profile.geti("count").get(index)) + " " + profile.gets("measure").get(index));
            }

            if ((profile.gets("photo").get(index)) != null) {
                File dir = new File(getExternalFilesDir(null), "profiles" + File.separator + "res" + File.separator + profile.get_name());
                File img = new File(dir, profile.gets("photo").get(index));
                try {
                    dir.mkdirs();
                    img.createNewFile();
                    image = Uri.fromFile(img);
                    ImageDecoder.Source imgSrc = ImageDecoder.createSource(this.getContentResolver(), image);

                    Thread thread = new Thread(() -> {
                        runOnUiThread(() ->
                        {
                            Bitmap bitmap = null;
                            try {
                                bitmap = ImageDecoder.decodeBitmap(imgSrc);
                                viewPhoto.setImageBitmap(bitmap);
                            } catch (IOException e) {
                            }
                        });
                    });

                    thread.start();

                } catch (IOException ignore) {}
            }

        }

    }

    public void bindImage(Uri image, ImageView viewPhoto) {

        try {
            ImageDecoder.Source imgSrc = ImageDecoder.createSource(this.getContentResolver(), image);
            Bitmap bitmap = ImageDecoder.decodeBitmap(imgSrc);
            viewPhoto.setImageBitmap(bitmap);
        } catch (IOException e) {

        }

    }

    public void viewFullImage(View view) {
        Intent intent = new Intent(this, ViewFullImageActivity.class);
        intent.putExtra("photo", image);
        startActivity(intent);
    }

}