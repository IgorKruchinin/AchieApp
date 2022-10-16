package ru.ikkui.achie.USM;


public interface Section {
    int get_format();
    void parse(String name) throws USMSectionException;
    String get_name();
    int size();
    void remove(int index);
}
