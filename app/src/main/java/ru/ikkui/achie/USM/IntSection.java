package ru.ikkui.achie.USM;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class IntSection implements Section, Serializable {
    private String name_;
    private final List<Long> objects_;
    public IntSection(String name) {
        name_ = name;
        objects_ = new Vector<>();
    }
    public IntSection(String name, Long[] vec) {
        name_ = name;
        objects_ = Arrays.asList(vec);
    }
    public int get_format() {
        return 0;
    }
    public String get_name() {
        return name_;
    }
    public void add(long object) {
        objects_.add(object);
    }
    public void remove(int index) {
        objects_.remove(index);
    }
    public void edit(int index, long newObject) {
        objects_.set(index, newObject);
    }
    public int size() {
        return objects_.size();
    }
    public final List<Long> getObjects_() {
        return objects_;
    }
    public final long get(int index) {
        return objects_.get(index);
    }
    public void parse(String str) throws USMSectionException {
        if (str.charAt(0) != 'i') {
            throw new USMSectionException("Non IntSection string given to parse method");
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
                //std::cout << sec_name;
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
                        objects_.add(Long.parseLong(obj_buff.toString()));
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
