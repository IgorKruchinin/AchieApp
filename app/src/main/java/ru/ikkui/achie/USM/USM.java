package ru.ikkui.achie.USM;
import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;

import ru.ikkui.achie.AddAchieActivity;

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
    public USM(final String name) {
        name_ = name;
        Path path = Paths.get("profiles", File.separator,  name_ + ".uto");
        //isecs_ = new HashMap<>();
        //ssecs_ = new HashMap<>();
        secs_ = new TreeMap<>();
        formats = new Vector<>();
        try {
            is_opened = true;
            for (String s: Files.readAllLines(path, StandardCharsets.UTF_8)) {
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
                Files.createFile(path);
                Files.write(Paths.get("profiles", File.separator, "profiles_list.txt"), name_.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.exit(1);
            }
        }
    }
    public USM(String name, int flag) {
        if (flag == 1) {
            try {
                name_ = name;
                //isecs_ = new HashMap<>();
                //ssecs_ = new HashMap<>();
                secs_ = new TreeMap<>();
                formats = new Vector<>();
                Path path = Paths.get("profiles", File.separator, name_ + ".uto");
                Files.createFile(path);
                Files.write(Paths.get("profiles", File.separator, "profiles_list.txt"), name_.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException ignored) {}
        }
    }
    public USM(final String name, final String program_name) {
        name_ = name;
        program_name_ = program_name;
        Path path = Paths.get("profiles", File.separator,  name_ + ".uto");
        //isecs_ = new HashMap<>();
        //ssecs_ = new HashMap<>();
        secs_ = new TreeMap<>();
        formats = new Vector<>();
        try {
            is_opened = true;
            for (String s: Files.readAllLines(path, StandardCharsets.UTF_8)) {
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
                Files.createFile(path);
                Files.write(Paths.get("profiles", File.separator, "profiles_list.txt"), (name_ + ":" + program_name).getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.exit(1);
            }
        }
    }
    public USM(String name, String program_name, int flag) {
        if (flag == 1) {
            try {
                name_ = name;
                program_name_ = program_name;
                //isecs_ = new HashMap<>();
                //ssecs_ = new HashMap<>();
                secs_ = new TreeMap<>();
                formats = new Vector<>();
                Path path = Paths.get("profiles", File.separator, name_ + ".uto");
                Files.createFile(path);
                Files.write(Paths.get("profiles", File.separator, "profiles_list.txt"), (name_ + ":" + program_name).getBytes(), StandardOpenOption.APPEND);
            } catch (IOException ignored) {}
        }
    }



    public USM(final Path dirPath, final String name) {
        name_ = name;
        Path path = Paths.get(dirPath.toAbsolutePath().toString(), File.separator, "profiles", File.separator,  name_ + ".uto");
        //isecs_ = new HashMap<>();
        //ssecs_ = new HashMap<>();
        secs_ = new TreeMap<>();
        formats = new Vector<>();
        try {
            is_opened = true;
            for (String s: Files.readAllLines(path, StandardCharsets.UTF_8)) {
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
                Files.createFile(path);
                Files.write(Paths.get(dirPath.toString(), File.separator, "profiles", File.separator, "profiles_list.txt"), name_.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.exit(1);
            }
        }
    }
    public USM(final Path dirPath, final String name, int flag) {
        if (flag == 1) {
            try {
                name_ = name;
                //isecs_ = new HashMap<>();
                //ssecs_ = new HashMap<>();
                secs_ = new TreeMap<>();
                formats = new Vector<>();
                Path path = Paths.get(dirPath.toString(), File.separator, "profiles", File.separator, name_ + ".uto");
                Files.createFile(path);
                Files.write(Paths.get(dirPath.toString(), File.separator, "profiles", File.separator, "profiles_list.txt"), name_.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException ignored) {}
        }
    }
    public USM(final Path dirPath, final String name, final String program_name) {
        name_ = name;
        program_name_ = program_name;
        Path path = Paths.get(dirPath.toString(), File.separator, "profiles", File.separator,  name_ + ".uto");
        //isecs_ = new HashMap<>();
        //ssecs_ = new HashMap<>();
        secs_ = new TreeMap<>();
        formats = new Vector<>();
        try {
            is_opened = true;
            for (String s: Files.readAllLines(path, StandardCharsets.UTF_8)) {
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
                Files.createFile(path);
                Files.write(Paths.get(dirPath.toString(), File.separator, "profiles", File.separator, "profiles_list.txt"), (name_ + ":" + program_name).getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.exit(1);
            }
        }
    }
    public USM(final Path dirPath, final String name, final String program_name, int flag) {
        if (flag == 1) {
            try {
                name_ = name;
                program_name_ = program_name;
                //isecs_ = new HashMap<>();
                //ssecs_ = new HashMap<>();
                secs_ = new TreeMap<>();
                formats = new Vector<>();
                Path path = Paths.get(dirPath.toString(), File.separator, "profiles", File.separator, name_ + ".uto");
                Files.createFile(path);
                Files.write(Paths.get(dirPath.toString(), File.separator, "profiles", File.separator, "profiles_list.txt"), (name_ + ":" + program_name).getBytes(), StandardOpenOption.APPEND);
            } catch (IOException ignored) {}
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
                    for (Integer obj: ((IntSection)entry.getValue()).getObjects_()) {
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
                    for (Integer obj : ((IntSection) entry.getValue()).getObjects_()) {
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
    public static List<USM> get_profiles(final String program_name) {
        List<USM> profiles = new Vector<>();
        Path path = Paths.get("profiles", File.separator,"profiles_list.txt");
        try {
            for (String s : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                String[] s1 = s.split(":");
                if (s1.length > 0) {
                    if (s1.length == 1 || s1[1].equals(program_name)) {
                        profiles.add(new USM(s1[0]));
                    }
                }
            }
        } catch (IOException e) {
            try {
                Files.createFile(path);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return profiles;
    }



    public static List<USM> get_profiles(Path dirPath) {
        List<USM> profiles = new Vector<>();
        Path path = Paths.get(dirPath.toString(), File.separator, "profiles", File.separator,"profiles_list.txt");
        try {
            for (String s : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                String[] s1 = s.split(":");
                profiles.add(new USM(s1[0]));
            }
        } catch (IOException e) {
            try {
                Files.createFile(path);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return profiles;
    }
    public static List<USM> get_profiles(Path dirPath, final String program_name) {
        List<USM> profiles = new Vector<>();
        Path path = Paths.get(dirPath.toString(), File.separator, "profiles", File.separator,"profiles_list.txt");
        try {
            for (String s : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                String[] s1 = s.split(":");
                if (s1.length > 0) {
                    if (s1.length == 1 || s1[1].equals(program_name)) {
                        profiles.add(new USM(s1[0]));
                    }
                }
            }
        } catch (IOException e) {
            try {
                Files.createFile(path);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
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