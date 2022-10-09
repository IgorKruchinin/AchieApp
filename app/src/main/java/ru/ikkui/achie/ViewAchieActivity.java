package ru.ikkui.achie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import java.io.File;
import java.io.IOException;

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

        TextView viewDate = findViewById(R.id.viewDate);
        TextView viewObject = findViewById(R.id.viewObject);
        TextView viewType = findViewById(R.id.viewType);
        TextView viewCount = findViewById(R.id.viewCount);
        ImageView viewPhoto = findViewById(R.id.viewPhoto);

        if (index < profile.gets("date").size()) {
            viewDate.setText(profile.gets("date").get(index));
            viewObject.setText(profile.gets("object").get(index));
            viewType.setText(profile.gets("type").get(index));
            viewCount.setText(String.valueOf(profile.geti("count").get(index)) + " " + profile.gets("measure").get(index));

            if ((profile.gets("photo").get(index)) != null) {
                File dir = new File(getExternalFilesDir(null), "profiles" + File.separator + "res" + File.separator + profile.get_name());
                File img = new File(dir, profile.gets("photo").get(index));
                try {
                    dir.mkdirs();
                    img.createNewFile();
                    image = Uri.fromFile(img);
                    Thread thread = new Thread(() -> {
                        bindImage(image, viewPhoto);
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