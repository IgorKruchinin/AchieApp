package ru.ikkui.achie.USSM.USM;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class USM implements Serializable {
    private static final long serialVersionUID = 1L;
    public String state = "";
    private String name_;
    private String program_name_ = "";
    //private Map<String, IntSection> isecs_;
    //private Map<String, StringSection> ssecs_;
    private SortedMap<String, Section> secs_;
    private List<Integer> formats;
    private boolean is_opened;

    public void reset(final USM anotherProfile) {
        name_ = anotherProfile.get_name();
        formats = anotherProfile.get_formats();
        secs_ = anotherProfile.secs_;
        is_opened = anotherProfile.opened();
        program_name_ = anotherProfile.get_program_name();
        state = "";
    }

    public USM(final String name, final String program_name, Context context) {
        name_ = name;
        // Path path = Paths.get("profiles", File.separator,  name_ + ".uto");
        //isecs_ = new HashMap<>();
        //ssecs_ = new HashMap<>();
        secs_ = new TreeMap<>();
        formats = new Vector<>();
        File uto = new  File (context.getExternalFilesDir(null), "profiles/" + name_ + ".uto");
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(uto)))) {
            is_opened = true;
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                if (s.charAt(0) == 'i') {
                    Section auto = new IntSection("auto");
                    auto.parse(s);
                    secs_.put(auto.get_name(), auto);
                    formats.add(0);
                } else if (s.charAt(0) == 's') {
                    Section auto = new StringSection("auto");
                    auto.parse(s);
                    secs_.put(auto.get_name(), auto);
                    formats.add(1);
                }
            }
        } catch (IOException | USMSectionException e) {
            is_opened = false;
        }
        if (!is_opened) {
            try {
                //Files.createFile(path);
                //Files.write(Paths.get("profiles", File.separator, "profiles_list.txt"), name_.getBytes(), StandardOpenOption.APPEND);
                File profiles_directory = new File(context.getExternalFilesDir(null), "profiles");
                File profiles_list = new File(context.getExternalFilesDir(null), "profiles" + File.separator +"profiles_list.txt");
                if (!profiles_directory.exists()) {
                    profiles_directory.mkdirs();
                }
                if (!profiles_list.exists()) {
                    profiles_list.createNewFile();
                }
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(profiles_list, true)));
                bufferedWriter.write(name_ + ":" + program_name + "\n");
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (IOException e) {
                //System.exit(1);
                state = e.toString();
            }
        }
    }












    public USM(final String name, Context context) {
        name_ = name;
         // Path path = Paths.get("profiles", File.separator,  name_ + ".uto");
        //isecs_ = new HashMap<>();
        //ssecs_ = new HashMap<>();
        secs_ = new TreeMap<>();
        formats = new Vector<>();
        File uto = new  File (context.getExternalFilesDir(null), "profiles/" + name_ + ".uto");
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(uto)))) {
            is_opened = true;
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                if (s.charAt(0) == 'i') {
                    Section auto = new IntSection("auto");
                    auto.parse(s);
                    secs_.put(auto.get_name(), auto);
                    formats.add(0);
                } else if (s.charAt(0) == 's') {
                    Section auto = new StringSection("auto");
                    auto.parse(s);
                    secs_.put(auto.get_name(), auto);
                    formats.add(1);
                }
            }
        } catch (IOException | USMSectionException e) {
            is_opened = false;
        }
        if (!is_opened) {
            try {
                //Files.createFile(path);
                //Files.write(Paths.get("profiles", File.separator, "profiles_list.txt"), name_.getBytes(), StandardOpenOption.APPEND);
                File profiles_directory = new File(context.getExternalFilesDir(null), "profiles");
                File profiles_list = new File(context.getExternalFilesDir(null), "profiles" + File.separator +"profiles_list.txt");
                if (!profiles_directory.exists()) {
                    profiles_directory.mkdirs();
                }
                if (!profiles_list.exists()) {
                    profiles_list.createNewFile();
                }
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(profiles_list, true)));
                bufferedWriter.write(name_ + "\n");
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (IOException e) {
                //System.exit(1);
                state = e.toString();
            }
        }
    }

    public File to_prof_archive(Context context) {
        try {
            BufferedInputStream o = null;
            File archive = new File(context.getExternalFilesDir(null) + File.separator + name_ + ".profpack");
            FileOutputStream to =  new FileOutputStream(archive);
            ZipOutputStream output = new ZipOutputStream(new BufferedOutputStream(to));
            byte[] data = new byte[512];

            FileInputStream utoInput = new FileInputStream(new File(context.getExternalFilesDir(null), "profiles" + File.separator + name_ + ".uto"));
            o = new BufferedInputStream(utoInput, 512);
            ZipEntry uosEntry = new ZipEntry(name_ + ".uto");
            output.putNextEntry(uosEntry);
            int k = 0;
            while((k = o.read(data, 0, 512)) != -1) {
                output.write(data, 0, k);
            }
            o.close();
            File resDir = new File(context.getExternalFilesDir(null), "profiles" + File.separator + "res" + File.separator + name_);
            if (resDir.listFiles() != null) {
                for (File inputFile : resDir.listFiles()) {
                    if (inputFile.exists() && inputFile.isFile()) {
                        FileInputStream inputStream = new FileInputStream(inputFile);
                        o = new BufferedInputStream(inputStream, 512);
                        ZipEntry entry = new ZipEntry("res" + File.separator + name_ + File.separator + inputFile.getName());
                        output.putNextEntry(entry);
                        int count = 0;
                        while ((count = o.read(data, 0, 512)) != -1) {
                            output.write(data, 0, count);
                        }
                        o.close();
                    }
                }
            }
            output.close();
            return archive;

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public File to_one_archive(Context context, final String filename, final String typename, int index, final String... resNames) throws IOException {
        try {

            Path path = Paths.get(context.getExternalFilesDir(null) + File.separator + "profiles", File.separator, name_ + ".uos");
            try (final OutputStream outputStream = Files.newOutputStream(path)) {
                StringBuilder text_buf = new StringBuilder();
                for (Map.Entry<String, Section> entry: secs_.entrySet()) {
                    if (entry.getValue() instanceof StringSection) {
                        text_buf.append(entry.getKey()).append(":");
                        text_buf.append(((StringSection)entry.getValue()).get(index));
                    } else if (entry.getValue() instanceof IntSection) {
                        text_buf.append(entry.getKey()).append(":");
                        text_buf.append(String.valueOf(((IntSection)entry.getValue()).get(index)));
                    }
                    text_buf.append("\n");
                }
                outputStream.write(text_buf.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }



            BufferedInputStream o = null;
            File archive = new File(context.getExternalFilesDir(null) + File.separator + filename + "." + typename);
            FileOutputStream to =  new FileOutputStream(archive);
            ZipOutputStream output = new ZipOutputStream(new BufferedOutputStream(to));
            byte[] data = new byte[512];

            FileInputStream uosInput = new FileInputStream(path.toString());
            o = new BufferedInputStream(uosInput, 512);
            ZipEntry uosEntry = new ZipEntry(path.getFileName().toString());
            output.putNextEntry(uosEntry);
            int k = 0;
            while((k = o.read(data, 0, 512)) != -1) {
                output.write(data, 0, k);
            }
            o.close();
            for (int i = 0; i < resNames.length; ++i) {
                System.out.println(resNames[i]);
                File inputFile = new File(context.getExternalFilesDir(null) + File.separator + "profiles" + File.separator + "res"+ File.separator + name_ + File.separator + resNames[i]);
                if (inputFile.exists() && inputFile.isFile()) {
                    FileInputStream inputStream = new FileInputStream(inputFile);
                    o = new BufferedInputStream(inputStream, 512);
                    ZipEntry entry = new ZipEntry("res" + File.separator + resNames[i]);
                    output.putNextEntry(entry);
                    int count = 0;
                    while ((count = o.read(data, 0, 512)) != -1) {
                        output.write(data, 0, count);
                    }
                    o.close();
                }
            }
            output.close();
            return archive;

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void from_profile_archive(ZipFile archive, Context context) throws IOException {
        Enumeration<? extends ZipEntry> entries = archive.entries();
        File profilesDir = new File(context.getExternalFilesDir(null), "profiles");
        File resDir = new File(profilesDir, "res");
        resDir.mkdirs();
        profilesDir.mkdirs();
        Vector<String> names = new Vector<String>();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                File dir = new File(context.getExternalFilesDir(null), "profiles" + File.separator + entry.getName());
                dir.mkdirs();
            } else {
                int index  = entry.getName().lastIndexOf('.');
                if (index >= 0 && entry.getName().substring(index + 1).equals("uto")) {
                    String name = entry.getName().substring(0, entry.getName().lastIndexOf('.'));
                    File res = new File(resDir, name);
                    res.mkdirs();
                    names.add(name);
                }
                InputStream inputStream = archive.getInputStream(entry);
                File file = new File(profilesDir, entry.getName());
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[512];
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
            }

        }
        File profiles_list = new File(context.getExternalFilesDir(null), "profiles" + File.separator +"profiles_list.txt");
        if (!profiles_list.exists()) {
            profiles_list.createNewFile();
        }
        for (String name : names) {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(profiles_list, true)));
            bufferedWriter.write(name + "\n");
            bufferedWriter.flush();
            bufferedWriter.close();
        }
    }
















    public final String get_name() {
        return name_;
    }
    public void to_file() {
        Path path = Paths.get("profiles", File.separator, name_ + ".uto");
        try (final OutputStream outputStream = Files.newOutputStream(path)) {
            StringBuilder text_buf = new StringBuilder();
            for (Map.Entry<String, Section> entry: secs_.entrySet()) {
                if (entry.getValue() instanceof StringSection) {
                    text_buf.append("s<").append(entry.getKey()).append(">");
                    for (String obj : ((StringSection)entry.getValue()).getObjects_()) {
                        text_buf.append(obj).append("|<\\e>");
                    }
                } else if (entry.getValue() instanceof IntSection) {
                    text_buf.append("i<").append(entry.getKey()).append(">");
                    for (Long obj: ((IntSection)entry.getValue()).getObjects_()) {
                        text_buf.append(obj).append("|<\\e>");
                    }
                }
                text_buf.append("\n");
            }
            outputStream.write(text_buf.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





    public void to_file(Context context) {
        //Path path = Paths.get("profiles", File.separator, name_ + ".uto");
        File uto = new File(context.getExternalFilesDir(null), "profiles" + File.separator + name_ + ".uto");
        try(BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(uto)))) {
            StringBuilder text_buf = new StringBuilder();
            for (Map.Entry<String, Section> entry : secs_.entrySet()) {
                if (entry.getValue() instanceof StringSection) {
                    text_buf.append("s<").append(entry.getKey()).append(">");
                    for (String obj : ((StringSection) entry.getValue()).getObjects_()) {
                        text_buf.append(obj).append("|<\\e>");
                    }
                } else if (entry.getValue() instanceof IntSection) {
                    text_buf.append("i<").append(entry.getKey()).append(">");
                    for (Long obj : ((IntSection) entry.getValue()).getObjects_()) {
                        text_buf.append(obj).append("|<\\e>");
                    }
                }
                text_buf.append("\n");
            }
            bufferedWriter.write(text_buf.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException f) {
            try {
                uto.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }





    public IntSection geti(final String name) {
        return (IntSection)secs_.get(name);
    }
    public StringSection gets(final String name) {
        return (StringSection)secs_.get(name);
    }
    public Section get(String name) {
        return secs_.get(name);
    }
    public final Collection<Section> getAll() {
        return secs_.values();
    }
    public void create_isec(String name) {
        secs_.put(name, new IntSection(name));
        formats.add(0);
    }
    public void create_ssec(String name) {
        secs_.put(name, new StringSection(name));
        formats.add(1);
    }
    public final List<Integer> get_formats() {
        return formats;
    }
    public final String get_program_name() {
        return program_name_;
    }
    public int size() {
        return secs_.size();
    }
    public final boolean opened() {
        return is_opened;
    }
    public static ArrayList<USM> get_profiles(Context context) {
        ArrayList<USM> profiles = new ArrayList<>();
        try {
            File profiles_directory = new File(context.getExternalFilesDir(null), "profiles");
            File profiles_list = new File(context.getExternalFilesDir(null), "profiles" + File.separator +"profiles_list.txt");
            if (!profiles_directory.exists()) {
                profiles_directory.mkdirs();
            }
            if (!profiles_list.exists()) {
                profiles_list.createNewFile();
            }
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(profiles_list)))) {
                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    String[] s1 = s.split(":");
                    profiles.add(new USM(s1[0], context));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return profiles;
    }
    public static List<USM> get_profiles(final String program_name, Context context) {

        ArrayList<USM> profiles = new ArrayList<>();
        try {
            File profiles_directory = new File(context.getExternalFilesDir(null), "profiles");
            File profiles_list = new File(context.getExternalFilesDir(null), "profiles" + File.separator +"profiles_list.txt");
            if (!profiles_directory.exists()) {
                profiles_directory.mkdirs();
            }
            if (!profiles_list.exists()) {
                profiles_list.createNewFile();
            }
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(profiles_list)))) {
                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    String[] s1 = s.split(":");
                    if (s1.length == 1 || s1[1].equals(program_name)) {
                        profiles.add(new USM(s1[0], "Achie", context));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return profiles;
    }
}

class Arithmetics {
    public static int computeExpression(String expression) {
        List<StringBuilder> sb = new Vector<>();
        List<Integer> op = new Vector<>();
        int k = -1;
        boolean crStrBld= true;
        for (int i = 0; i < expression.length(); ++i) {
            char c = expression.charAt(i);
            if (c != ' ') {
                if (c == '+') {
                    op.add(0);
                    crStrBld = true;
                }  else if (c == '-') {
                    op.add(1);
                    crStrBld = true;
                }  else if (c == '*') {
                    op.add(2);
                    crStrBld = true;
                }  else if (c == '/') {
                    op.add(3);
                    crStrBld = true;
                } else {
                    if (crStrBld) {
                        sb.add(new StringBuilder());
                        ++k;
                        crStrBld = false;
                    } else {
                        sb.get(k).append(c);
                    }
                }
            }
        }
        int result = Integer.parseInt(sb.get(0).toString());
        for (int i = 1, j = 0; i < sb.size() && j < sb.size() - 1; ++i, ++j) {
            switch (op.get(j)) {
                case 0:
                    result += Integer.parseInt(sb.get(i).toString());
                    break;
                case 1:
                    result -= Integer.parseInt(sb.get(i).toString());
                    break;
                case 2:
                    result *= Integer.parseInt(sb.get(i).toString());
                    break;
                case 3:
                    result /= Integer.parseInt(sb.get(i).toString());
                    break;
            }
        }
        return result;
    }
}