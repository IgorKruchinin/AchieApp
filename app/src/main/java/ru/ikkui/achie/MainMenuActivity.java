package ru.ikkui.achie;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;
import java.util.zip.ZipFile;

import ru.ikkui.achie.USSM.USM.IntSection;
import ru.ikkui.achie.USSM.USM.Section;
import ru.ikkui.achie.USSM.USM.StringSection;
import ru.ikkui.achie.USSM.USM.USM;
import ru.ikkui.achie.databinding.ActivityMainMenuBinding;

public class MainMenuActivity extends AppCompatActivity {

    Vector<File> usedFiles;


    ActivityResultLauncher<String> getProfArchivePath = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            try {
                File file = new File(result.getPath());
                File to = new File(getExternalCacheDir(), "import.zip");
                to.createNewFile();
                InputStream input = getContentResolver().openInputStream(result);
                FileOutputStream output = new FileOutputStream(to);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                ZipFile zipFile = new ZipFile(to);
                USM.from_profile_archive(zipFile, MainMenuActivity.this);
                profile = new USM(profile.get_name(), "Achie", MainMenuActivity.this);
                adapter.notifyDataSetChanged();
                to.delete();
            } catch (IOException ex) {
                Toast.makeText(MainMenuActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }
        }
    });

    class AddAchieDialog extends  Dialog {
        USM profile;
        USMAdapter adapter;

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
        // private String imagePath = "";

        AddAchieDialog(Context context, USM profile, USMAdapter adapter) {
            super(context);
            this.profile = profile;
            this.adapter = adapter;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.content_add_achie);


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
                    datePicker = new DatePickerDialog(MainMenuActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            calendar.set(year, month, day);
                            date.setTime(calendar.getTime().getTime());
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
            Button loadImageBtn = findViewById(R.id.loadImageBtn);
            loadImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getImagePath.launch("image/*");
                }
            });
            Button addBtn = findViewById(R.id.achieAddBtn);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast toast = Toast.makeText(MainMenuActivity.this, profile.state, Toast.LENGTH_LONG);
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
                    String achieCount = achieCountFld.getText().toString();
                    if (!(achieCount).equals("")) {
                        profile.geti("count").add(Integer.parseInt(achieCount));
                    } else {
                        profile.geti("count").add(-1);
                    }
                    try {
                        profile.gets("photo").add(imagePath);
                        imagePath = "";
                    } catch (NullPointerException e) {
                        profile.create_ssec("photo");
                        profile.gets("photo").add(imagePath);
                        imagePath = "";
                    }
                    profile.to_file(MainMenuActivity.this);
                    adapter.notifyDataSetChanged();
                    cancel();
                }
            });
        }
    }

    class EditAchieDialog extends  Dialog {
        USM profile;
        USMAdapter adapter;

        int position;

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

        // private String imagePath = "";

        EditAchieDialog(Context context, USM profile, USMAdapter adapter, int position) {
            super(context);
            this.profile = profile;
            this.adapter = adapter;
            this.position = position;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.content_add_achie);

            achieDateFld = findViewById(R.id.achieDateFld);
            dateFormat = SimpleDateFormat.getDateInstance();

            achieDateFld.setInputType(InputType.TYPE_NULL);
            date = new Date(profile.geti("date").get(position));
            achieDateFld.setText(dateFormat.format(date));
            achieDateFld.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);
                    date.setTime(calendar.getTime().getTime());
                    datePicker = new DatePickerDialog(MainMenuActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            calendar.set(year, month, day);
                            date.setTime(calendar.getTime().getTime());
                            achieDateFld.setText(dateFormat.format(date));
                        }
                    }, year, month, day);
                    datePicker.show();
                }
            });

            achieObjectFld = findViewById(R.id.achieObjectFld);
            achieObjectFld.setText(profile.gets("object").get(position));
            achieObject  = profile.gets("object").getObjects_();
            achieObjectFld.setAutofillHints(achieObject.toArray(new String[achieObject.size()]));
            achieTypeFld = findViewById(R.id.achieTypeFld);
            achieTypeFld.setText(profile.gets("type").get(position));
            achieType = profile.gets("type").getObjects_();
            achieTypeFld.setAutofillHints(achieType.toArray(new String[achieType.size()]));
            achieMeasureFld = findViewById(R.id.achieMeasureFld);
            achieMeasureFld.setText(profile.gets("measure").get(position));
            achieMeasure = profile.gets("measure").getObjects_();
            achieMeasureFld.setAutofillHints(achieMeasure.toArray(new String[achieMeasure.size()]));
            achieCountFld = findViewById(R.id.achieCountFld);
            achieCountFld.setText(String.valueOf(profile.geti("count").get(position)));
            Button loadImageBtn = findViewById(R.id.loadImageBtn);
            loadImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File oldFile = new File(getExternalFilesDir(null), "profiles" + File.separator + "res" + File.separator + profile.get_name() + File.separator + profile.gets("photo").get(position));
                    oldFile.delete();
                    getImagePath.launch("image/*");
                }
            });
            Button addBtn = findViewById(R.id.achieAddBtn);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast toast = Toast.makeText(MainMenuActivity.this, profile.state, Toast.LENGTH_LONG);
                    toast.show();
                    if (!profile.opened()) {
                        profile.create_isec("date");
                        profile.create_ssec("object");
                        profile.create_ssec("type");
                        profile.create_ssec("measure");
                        profile.create_isec("count");
                        profile.create_ssec("photo");
                    }


                    profile.geti("date").edit(position, date.getTime());
                    profile.gets("object").edit(position, achieObjectFld.getText().toString());
                    profile.gets("type").edit(position, achieTypeFld.getText().toString());
                    profile.gets("measure").edit(position, achieMeasureFld.getText().toString());
                    profile.geti("count").edit(position, Integer.parseInt(achieCountFld.getText().toString()));
                    try {
                        profile.gets("photo").edit(position, imagePath);
                        imagePath = "";
                    } catch (NullPointerException e) {
                        profile.create_ssec("photo");
                        profile.gets("photo").add(imagePath);
                        imagePath = "";
                    }
                    profile.to_file(MainMenuActivity.this);
                    adapter.notifyDataSetChanged();
                    cancel();
                }
            });
        }
    }

    class ViewAchieDialog extends Dialog {
        protected USM profile;
        protected final int index;
        Uri image;

        public ViewAchieDialog(Context context, USM profile, final int index) {
            super(context);
            this.profile = profile;
            this.index = index;
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.content_view_achie);
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
                        File archive = profile.to_one_archive(MainMenuActivity.this, profile.get_name(), "ach", index, profile.gets("photo").get(index));
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("application/x-zip-compressed");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(MainMenuActivity.this, BuildConfig.APPLICATION_ID, archive));
                        shareIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(shareIntent, "Send File"));
                        usedFiles.add(archive);
                    } catch (IOException ex) {
                        Toast.makeText(MainMenuActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();;
                    }
                }
            });



            DateFormat dateFormat = SimpleDateFormat.getDateInstance();

            if (index < profile.geti("date").size()) {
                java.util.Date date = new java.util.Date(profile.geti("date").get(index));
                viewDate.setText(dateFormat.format(date));
                viewObject.setText(profile.gets("object").get(index));
                viewType.setText(profile.gets("type").get(index));
                if (profile.geti("count").get(index) >= 0) {
                    viewCount.setText(String.valueOf(profile.geti("count").get(index)) + " " + profile.gets("measure").get(index));
                }
                viewPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewFullImage(view);
                    }
                });

                if ((profile.gets("photo").get(index)) != null) {
                    File dir = new File(getExternalFilesDir(null), "profiles" + File.separator + "res" + File.separator + profile.get_name());
                    File img = new File(dir, profile.gets("photo").get(index));
                    try {
                        dir.mkdirs();
                        img.createNewFile();
                        image = Uri.fromFile(img);
                        ImageDecoder.Source imgSrc = ImageDecoder.createSource(MainMenuActivity.this.getContentResolver(), image);

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
                ImageDecoder.Source imgSrc = ImageDecoder.createSource(MainMenuActivity.this.getContentResolver(), image);
                Bitmap bitmap = ImageDecoder.decodeBitmap(imgSrc);
                viewPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {

            }

        }

        public void viewFullImage(View view) {
            Intent intent = new Intent(MainMenuActivity.this, ViewFullImageActivity.class);
            intent.putExtra("photo", image);
            startActivity(intent);
        }
    }

    class DefaultProfileDialog extends  Dialog {

        private USM profile;

        DefaultProfileDialog(Context context) {
            super(context);
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.default_profile_dialog);

            TextView notice = findViewById(R.id.defaultProfileNotice);
            notice.setText(R.string.create_new_default_profile_message);

            EditText defaultProfileName = findViewById(R.id.defaultProfileNameFld);
            Button applyProfileBtn = findViewById(R.id.applyProfileBtn);
            View.OnClickListener applyProfileBtnListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File defaultProfileFile = new File(getExternalCacheDir(), "default_profile.txt");
                    try {
                        defaultProfileFile.createNewFile();
                        USM profile = (new USM(defaultProfileName.getText().toString(), "Achie", MainMenuActivity.this));
                        profile.create_isec("date");
                        profile.create_ssec("object");
                        profile.create_ssec("type");
                        profile.create_ssec("measure");
                        profile.create_isec("count");
                        profile.create_ssec("photo");
                        profile.to_file(MainMenuActivity.this);
                        DefaultProfileDialog.this.profile = profile;
                        BufferedWriter defaultProfileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(defaultProfileFile)));
                        defaultProfileWriter.write(defaultProfileName.getText().toString());
                        defaultProfileWriter.flush();
                        defaultProfileWriter.close();
                        DefaultProfileDialog.super.cancel();
                    } catch (IOException e) {
                        Toast.makeText(MainMenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        DefaultProfileDialog.super.cancel();

                    }
                }
            };
            applyProfileBtn.setOnClickListener(applyProfileBtnListener);
        }
        public USM getProfile() {
            return profile;
        }
    }

    public class OpenTerminalDialog extends Dialog {
        public OpenTerminalDialog(Context context) {
            super(context);
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.open_terminal_dialog);

            Button cancelBtn = findViewById(R.id.cancelBtn);
            Button OKButton = findViewById(R.id.OKBtn);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancel();
                }
            });
            OKButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent terminalActivity = new Intent(MainMenuActivity.this, LOQTerm.class);
                    startActivity(terminalActivity);
                    cancel();
                }
            });
        }
    }

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainMenuBinding binding;
    private Button addAchieBtn;
    private USM profile;
    private USMAdapter adapter;
    String imagePath = "";

    boolean profileWereNullFlag = false;


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
            String newFileName = count + from.getName().substring(from.getName().lastIndexOf("."));
            File to = new File(dir, newFileName);
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

                imagePath = newFileName;
            } catch (IOException e) {
                Toast.makeText(MainMenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent currIntent = getIntent();
        String action = currIntent.getAction();
        if (action.compareTo(Intent.ACTION_VIEW) == 0) {
            String scheme = currIntent.getScheme();
            ContentResolver contentResolver = getContentResolver();
            if (scheme.compareTo(contentResolver.SCHEME_CONTENT) == 0) {
                Uri result = currIntent.getData();
                try {
                    File file = new File(result.getPath());
                    File to = new File(getExternalCacheDir(), "import.zip");
                    to.createNewFile();
                    InputStream input = getContentResolver().openInputStream(result);
                    FileOutputStream output = new FileOutputStream(to);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = input.read(buffer)) > 0) {
                        output.write(buffer, 0, length);
                    }
                    ZipFile zipFile = new ZipFile(to);
                    USM.from_profile_archive(zipFile, MainMenuActivity.this);
                    profile = new USM(profile.get_name(), "Achie", MainMenuActivity.this);
                    adapter.notifyDataSetChanged();
                    to.delete();
                } catch (IOException ex) {
                    Toast.makeText(MainMenuActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
            }

        }

        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        usedFiles = new Vector<>();

        // Bundle arguments = getIntent().getExtras();

        // profile = (USM) arguments.get("profile");
        getDefaultProfile();

        if (profile == null) {
            DefaultProfileDialog dialog = new DefaultProfileDialog(this);
            dialog.show();
            getDefaultProfile();
            profileWereNullFlag = true;
        } else {
            init();
        }



    }

    private void init() {
        if (profile != null) {
            USMAdapter.OnAchieClickListener achieClickListener = new USMAdapter.OnAchieClickListener() {
                @Override
                public void onAchieClick(USM profile, int position) {
                    /*Intent intent = new Intent(MainMenuActivity.this, ViewAchieActivity.class);
                    intent.putExtra("profile", profile);
                    intent.putExtra("index", position);
                    startActivity(intent);*/
                    ViewAchieDialog viewAchieDialog = new ViewAchieDialog(MainMenuActivity.this, profile, position);
                    viewAchieDialog.show();
                }

                @Override
                public void onAchieLongClick(View view, USM profile, int position) {
                    PopupMenu menu = new PopupMenu(MainMenuActivity.this, view);
                    menu.getMenuInflater().inflate(R.menu.achie_popup_menu, menu.getMenu());
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.achieTextShareItem:
                                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                    Date date = new Date(profile.geti("date").get(position));
                                    DateFormat dateFormat = SimpleDateFormat.getDateInstance();
                                    File photo = new File(getExternalFilesDir(null), "profiles" + File.separator + "res" + File.separator + profile.get_name() + File.separator + profile.gets("photo").get(position));
                                    String additional = "";
                                    if (profile.geti("count").get(position) != -1) {
                                        additional = "\n\n" + profile.geti("count").get(position) + " " + profile.gets("measure").get(position);
                                    }
                                    String shareString = dateFormat.format(date) + "\n\n" + profile.gets("object").get(position) + "\n\n" + profile.gets("type").get(position) + additional;
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);
                                    if (photo.exists() && photo.isFile()) {
                                        shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(MainMenuActivity.this, BuildConfig.APPLICATION_ID, photo));
                                        shareIntent.setType("image/*");
                                    } else {
                                        shareIntent.setType("text/plain");
                                    }
                                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    startActivity(Intent.createChooser(shareIntent, "Send"));
                                    break;
                                case R.id.edit_achie:
                                    EditAchieDialog editAchieDialog = new EditAchieDialog(MainMenuActivity.this, profile, adapter, position);
                                    editAchieDialog.show();
                                    // Intent editIntent = new Intent(MainMenuActivity.this, AchieEditActivity.class);
                                    // editIntent.putExtra("profile", profile);
                                    // editIntent.putExtra("index", position);
                                    // startActivity(editIntent);
                                    break;
                                case R.id.deleteAchie:
                                    profile.geti("date").remove(position);
                                    profile.gets("object").remove(position);
                                    profile.gets("type").remove(position);
                                    profile.gets("measure").remove(position);
                                    profile.geti("count").remove(position);
                                    profile.gets("photo").remove(position);
                                    profile.to_file(MainMenuActivity.this);
                                    adapter.notifyDataSetChanged();
                                    break;

                            }
                            return true;
                        }
                    });
                    menu.show();
                }
            };

            RecyclerView recyclerView = findViewById(R.id.achies_list);

            adapter = new USMAdapter(this, profile, achieClickListener);

            DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(divider);
        }


        /*List<String> achiesStrings = new Vector<String>();
        for (int i = 0; i < profile.size(); ++i) {
            achiesStrings.add(profile.geti("date").get(i) + " " + profile.gets("object").get(i));
        }
        Spinner achiesList = findViewById(R.id.listAchies);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, achiesStrings);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        achiesList.setAdapter(arrayAdapter);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.selectProfileItem:
                class SelectProfileDialog extends  Dialog {
                    USM profile;
                    USMAdapter adapter;
                    SelectProfileDialog(Context context, USM profile, USMAdapter adapter) {
                        super(context);
                        this.profile = profile;
                        this.adapter = adapter;
                    }
                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        setContentView(R.layout.select_profile_dialog);

                        List<USM> profiles = USM.get_profiles("Achie", MainMenuActivity.this);
                        RecyclerView rec = findViewById(R.id.select_profiles);
                        rec.setLayoutManager(new LinearLayoutManager(MainMenuActivity.this));
                        ProfileAdapter.OnProfileClickListener profileClickListener = new ProfileAdapter.OnProfileClickListener() {
                            @Override
                            public void onProfileClick(USM selectedProfile, int position) {
                                profile.reset(selectedProfile);
                                adapter.reset(profile);
                                Toast.makeText(MainMenuActivity.this, profile.get_name(), Toast.LENGTH_SHORT).show();
                                cancel();
                            }
                            @Override
                            public void onProfileLongClick(View view, USM selectedProfile, int position)
                            {
                                PopupMenu menu = new PopupMenu(MainMenuActivity.this, view);
                                menu.getMenuInflater().inflate(R.menu.profile_popup_menu, menu.getMenu());
                                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        switch (menuItem.getItemId()) {
                                            case R.id.ExportProfItem:
                                                File archive = selectedProfile.to_prof_archive(MainMenuActivity.this);
                                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                                shareIntent.setType("application/x-zip-compressed");
                                                shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(MainMenuActivity.this, BuildConfig.APPLICATION_ID, archive));
                                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                startActivity(Intent.createChooser(shareIntent, "Send File"));
                                                usedFiles.add(archive);
                                                return true;
                                            default:
                                                return false;
                                        }
                                    }
                                });
                                menu.show();
                            }
                        };
                        ProfileAdapter profileAdapter = new ProfileAdapter(MainMenuActivity.this, profiles, profileClickListener);
                        profileAdapter = new ProfileAdapter(MainMenuActivity.this, profiles, profileClickListener);
                        DividerItemDecoration divider = new DividerItemDecoration(MainMenuActivity.this, DividerItemDecoration.VERTICAL);
                        rec.setAdapter(profileAdapter);
                        rec.addItemDecoration(divider);

                    }
                }
                SelectProfileDialog profileDialog = new SelectProfileDialog(this, profile, adapter);
                profileDialog.show();
                return true;
            case R.id.addProfileItem:
                class AddProfileDialog extends  Dialog {
                    USM profile;
                    USMAdapter adapter;
                    AddProfileDialog(Context context, USM profile, USMAdapter adapter) {
                        super(context);
                        this.profile = profile;
                        this.adapter = adapter;
                    }
                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        setContentView(R.layout.default_profile_dialog);
                        TextView notice = findViewById(R.id.defaultProfileNotice);
                        notice.setText(R.string.new_profile_message);

                        EditText defaultProfileName = findViewById(R.id.defaultProfileNameFld);
                        Button applyProfileBtn = findViewById(R.id.applyProfileBtn);

                        View.OnClickListener applyProfileBtnListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                USM newProfile = (new USM(defaultProfileName.getText().toString(), "Achie", MainMenuActivity.this));
                                newProfile.create_isec("date");
                                newProfile.create_ssec("object");
                                newProfile.create_ssec("type");
                                newProfile.create_ssec("measure");
                                newProfile.create_isec("count");
                                newProfile.create_ssec("photo");
                                newProfile.to_file(MainMenuActivity.this);
                                profile.reset(newProfile);
                                adapter.notifyDataSetChanged();
                                AddProfileDialog.super.cancel();
                            }
                        };
                        applyProfileBtn.setOnClickListener(applyProfileBtnListener);
                    }
                }
                AddProfileDialog dialog = new AddProfileDialog(MainMenuActivity.this, profile, adapter);
                dialog.show();
                return true;
            case R.id.makeDefaultItem:
                try {
                    File defaultProfileFile = new File(getExternalCacheDir(), "default_profile.txt");
                    BufferedWriter defaultProfileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(defaultProfileFile)));
                    defaultProfileWriter.write(profile.get_name());
                    defaultProfileWriter.flush();
                    defaultProfileWriter.close();
                } catch (IOException e) {
                    Toast.makeText(MainMenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            case R.id.ImportProfileItem:
                getProfArchivePath.launch("*/*");
                return true;
            case R.id.openTerminal:
                OpenTerminalDialog openTerminalDialog = new OpenTerminalDialog(MainMenuActivity.this);
                openTerminalDialog.show();
                return true;
            default:
                return false;
        }
    }

    public void addAchie(View v) {
        if (profileWereNullFlag) {
            getDefaultProfile();
            init();
        }
        AddAchieDialog achieDialog = new AddAchieDialog(MainMenuActivity.this, profile, adapter);
        achieDialog.show();
            /*PopupMenu menu = new PopupMenu(MainMenuActivity.this, v);
            menu.getMenuInflater().inflate(R.menu.popup_menu, menu.getMenu());
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.addAchie:
                            AddAchieDialog achieDialog = new AddAchieDialog(MainMenuActivity.this, profile, adapter);
                            achieDialog.show();
                            // Intent intent = new Intent(MainMenuActivity.this, AddAchieActivity.class);
                            // intent.putExtra("profile", profile);
                            // startActivity(intent);
                            break;
                        case R.id.addToPlan:
                            Toast toast = Toast.makeText(MainMenuActivity.this, "This content not realised yet", Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                    }
                    return true;
                }
            });
            menu.show();*/
    }

    public void to_csv() {
        File csv = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), profile.get_name() + ".csv");
        try {
            if (!csv.exists()) {
                csv.createNewFile();
            }
            BufferedWriter csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv, false)));
            int size = 0;
            for (Section s: profile.getAll()) {
                size = s.size();
                csvWriter.write(s.get_name());
                csvWriter.write(",");
                //if (s.size() < min_size) {
                //min_size = s.size();
                //}
            }
            csvWriter.write("\n");
            for (int index = 0; index < size; ++index) {
                for (Section s : profile.getAll()) {
                    if (s instanceof StringSection) {
                        csvWriter.write(((StringSection)s).get(index));
                    } if (s instanceof IntSection) {
                        csvWriter.write(String.valueOf(((IntSection)s).get(index)));
                    }
                    csvWriter.write(",");
                }
                csvWriter.write("\n");
            }
            csvWriter.flush();
            csvWriter.close();
            Toast.makeText(this, csv.getPath().toString(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getDefaultProfile() {
        try {
            BufferedReader readDefaultProfile = new BufferedReader(new InputStreamReader(new FileInputStream(new File(getExternalCacheDir(), "default_profile.txt"))));
            String defaultProfileName = readDefaultProfile.readLine();
            profile = new USM(defaultProfileName, "Achie", this);
            Toast.makeText(this, defaultProfileName, Toast.LENGTH_SHORT).show();
            readDefaultProfile.close();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        for (File file : usedFiles) {
            file.delete();
        }
        super.onDestroy();
    }

}