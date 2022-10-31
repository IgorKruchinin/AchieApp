package ru.ikkui.achie.USSM.USM;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class StringSection implements Section, Serializable {
    private String name_;
    private final List<String> objects_;
    public StringSection(String name) {
        name_ = name;
        objects_ = new Vector<>();
    }
    public int get_format() {
        return 1;
    }
    public final String get_name() {
        return name_;
    }
    public StringSection(String name, String[] vec) {
        name_ = name;
        objects_ = Arrays.asList(vec);
    }
    public void add(String object) {
        objects_.add(object);
    }
    public void remove(int index) {
        objects_.remove(index);
    }
    public void edit(int index, String newObject) {
        objects_.set(index, newObject);
    }
    public int size() {
        return objects_.size();
    }
    public final String get(int index) {
        return objects_.get(index);
    }
    public final List<String> getObjects_() {
        return objects_;
    }
    public void parse(String str) throws USMSectionException {
        if (str.charAt(0) != 's') {
            throw new USMSectionException("Non StringSection string given to parse method");
        }
        StringBuilder sec_name = new StringBuilder();
        boolean first_in = true;
        boolean continue_reading = false;
        boolean name_entered = false;
        boolean first_after_init_flag = true;
        int cnt = 0;
        StringList sl = new StringList('1');
        StringBuilder obj_buff = new StringBuilder();
        for (int i = 0; i < str.length(); ++i) {
            char s = str.charAt(i);
            if (s == '<' && first_in)
                first_in = false;
            else if (!first_in && !name_entered && s != '>') {
                sec_name.append(s);
            } else if (s == '>' && !first_in && !name_entered) {
                name_entered = true;
                continue_reading = true;
                continue;
            }
            if (continue_reading) {
                sl.add(s);
                if (cnt < 4) {
                    if (first_after_init_flag) {
                        sl = sl.next;
                        first_after_init_flag = false;
                    }
                } else {
                    if (!(sl.to_string(1, 5).equals("<\\e>"))) {
                        obj_buff.append(sl.c);
                    } else {
                        objects_.add(obj_buff.toString());
                        obj_buff = new StringBuilder();
                        sl = sl.end();
                        cnt = 0;
                        first_after_init_flag = true;
                        continue;
                    }
                    sl = sl.next;
                }
                ++cnt;
            }
        }
        name_ = sec_name.toString();
    }
}
