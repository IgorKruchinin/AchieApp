package ru.ikkui.achie.USSM.USSM.LOQ;

import android.content.Context;

import ru.ikkui.achie.USSM.USM.*;

import java.util.*;

public class LOQ {
    private SortedMap<String, USM> profiles;
    private Stack<Long> integers;
    private Stack<String> strings;
    private String prof_name = "";
    private int lastFormat = -1;
    private boolean is_changed;
    private boolean lock_flag;
    private String prog_name;

    private Context context;
    public LOQ(final String prog_name, Context context) {
        this.prog_name = prog_name;
        this.context = context;
        profiles = new TreeMap<>();
        for (USM prof: USM.get_profiles(prog_name, context)) {
            profiles.put(prof.get_name(), prof);
        }
        integers = new Stack<>();
        strings = new Stack<>();
        is_changed = false;
        lock_flag = false;
    }
    public void parseQuery(String query) throws LOQNoProfileException {
        is_changed = false;
        boolean create_flag = false;
        boolean post_add_flag = false;
        boolean entry_flag = false;
        boolean write_flag = false;
        boolean name_write_flag = false;
        boolean writing_flag = false;
        boolean get_flag = false;
        boolean set_flag = false;
        boolean cont_set_flag = false;
        boolean setting_flag = false;
        boolean getting_flag = false;
        boolean add_value_flag = false;
        boolean adding_value_flag = false;
        int index = 0;
        //String prof_name = "";
        String name = "";
        int format = -1;
        for (String q: query.split(" ")) {
            if (create_flag) {
                create_flag = false;
                if (prof_name.equals("")) {
                    if (lock_flag) {
                        profiles.put(q, new USM(q, prog_name, context));
                    } else {
                        profiles.put(q, new USM(q, context));
                    }
                    prof_name = q;
                } else {
                    switch (q) {
                        case "int":
                            format = 0;
                            break;
                        case "str":
                            format = 1;
                            break;
                    }
                    post_add_flag = true;
                }
                continue;
            } else if (post_add_flag) {
                post_add_flag = false;
                name = q;
                switch(format) {
                    case 0:
                        profiles.get(prof_name).create_isec(name);
                        profiles.get(prof_name).to_file(context);
                        name = "";
                        break;
                    case 1:
                        profiles.get(prof_name).create_ssec(name);
                        profiles.get(prof_name).to_file(context);
                        name = "";
                        break;
                }
                continue;
            } else if (entry_flag) {
                entry_flag = false;
                if (profiles.containsKey(q)) {
                    prof_name = q;
                } else {
                    throw new LOQNoProfileException("No profile named " + q);
                }
                continue;
            } else if (write_flag) {
                write_flag = false;
                switch(q) {
                    case "int":
                        format = 0;
                        break;
                    case "str":
                        format = 1;
                        break;
                }
                name_write_flag = true;
                continue;
            } else if (name_write_flag) {
                name_write_flag = false;
                name = q;
                writing_flag = true;
                continue;
            } else if(writing_flag) {
                writing_flag = false;
                switch(format) {
                    case 0:
                        profiles.get(prof_name).geti(name).add(Integer.parseInt(q));
                        profiles.get(prof_name).to_file(context);
                        break;
                    case 1:
                        profiles.get(prof_name).gets(name).add(q);
                        profiles.get(prof_name).to_file(context);
                        break;
                }
                continue;
            } else if (get_flag) {
                get_flag = false;
                name = q;
                getting_flag = true;
                continue;
            }  else if (getting_flag) {
                getting_flag = false;
                index = Integer.parseUnsignedInt(q);
                if (profiles.get(prof_name).get(name) instanceof IntSection) {
                    lastFormat = 0;
                    integers.push(profiles.get(prof_name).geti(name).get(index));
                    is_changed = true;
                } else if (profiles.get(prof_name).get(name) instanceof StringSection) {
                    lastFormat = 1;
                    strings.push(profiles.get(prof_name).gets(name).get(index));
                    is_changed = true;
                }
                continue;
            } else if (set_flag) {
                name = q;
                cont_set_flag = true;
                set_flag = false;
                continue;
            } else if (cont_set_flag) {
                index = Integer.parseUnsignedInt(q);
                setting_flag = true;
                cont_set_flag = false;
                continue;
            } else if (setting_flag) {
                if (profiles.get(prof_name).get(name) instanceof IntSection) {
                    profiles.get(prof_name).geti(name).edit(index, Long.parseLong(q));
                    profiles.get(prof_name).to_file(context);
                } else if (profiles.get(prof_name).get(name) instanceof StringSection) {
                    profiles.get(prof_name).gets(name).edit(index, q);
                    profiles.get(prof_name).to_file(context);
                }
                setting_flag = false;
                continue;
            } else if (add_value_flag) {
                add_value_flag = false;
                name = q;
                adding_value_flag = true;
                continue;
            } else if (adding_value_flag) {
                adding_value_flag = false;
                if (profiles.get(prof_name).get(name) instanceof IntSection) {
                    ((IntSection)profiles.get(prof_name).get(name)).add(Integer.parseInt(q));
                    profiles.get(prof_name).to_file(context);
                    name = "";
                } else if (profiles.get(prof_name).get(name) instanceof StringSection) {
                    ((StringSection)profiles.get(prof_name).get(name)).add(q);
                    profiles.get(prof_name).to_file(context);
                    name = "";
                }
                continue;
            }
            switch(q) {
                case "create":
                    create_flag = true;
                    break;
                case "add":
                    add_value_flag = true;
                    break;
                case "entry":
                    entry_flag = true;
                    break;
                case "write":
                    write_flag = true;
                    break;
                case "get":
                    get_flag = true;
                    break;
                case "set":
                    set_flag = true;
                    break;
                case "table":
                    AsTable(profiles.get(prof_name));
                    break;
                case "lock":
                    lock_flag = true;
                    break;
                case "unlock":
                    lock_flag = false;
                    break;
                case "lock_status":
                    getLockStatus();
                    break;
                case "exit":
                    prof_name = "";
                    break;
                case "quit":
                    System.exit(0);
            }
        }
    }
    public long popInt() {
        return integers.pop();
    }
    public String popStr() {
        return strings.pop();
    }
    public int getLastFormat() {
        return lastFormat;
    }
    public String getProfName() {
        return prof_name;
    }
    public final boolean changed() {
        return is_changed;
    }
    public final boolean getLockStatus() {
        is_changed = true;
        lastFormat = 3;
        return lock_flag;
    }
    private List<String> AsTable(USM profile) {
        List<String> secLst = new Vector<>();
        for (Section s: profile.getAll()) {
            if (s instanceof StringSection) {
                for (int i = 0; i < s.size(); ++i) {
                    secLst.add(((StringSection)s).get(i));
                }
            } else if (s instanceof IntSection) {
                for (int i = 0; i < s.size(); ++i) {
                    secLst.add(String.valueOf(((IntSection)s).get(i)));
                }
            }
        }
        return secLst;
    }
}
