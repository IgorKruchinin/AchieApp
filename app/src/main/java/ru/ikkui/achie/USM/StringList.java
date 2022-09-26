package ru.ikkui.achie.USM;


public class StringList {
    char c;
    StringList next;
    public StringList(final char cc) {
        c = cc;
    }
    public void add(char cc) {
        StringList sl = this;
        while(sl.next != null) {
            sl = sl.next;
        }
        sl.next = new StringList(cc);
    }
    public final String to_string(int beg, int end) {
        StringBuilder str = new StringBuilder();
        StringList sl = this;
        int counter = 0;
        while(sl != null && counter < end) {
            // Possible error
            if (counter >= beg) {
                str.append(sl.c);
            }
            sl = sl.next;
            ++counter;
        }
        return str.toString();
    }
    StringList end() {
        StringList sl = this;
        while (sl.next != null) {
            sl = sl.next;
        }
        return sl;
    }
}
