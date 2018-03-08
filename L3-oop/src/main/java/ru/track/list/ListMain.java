package ru.track.list;

/**
 *
 */
public class ListMain {
    public static void main(String[] args) {
        MyArrayList l = new MyArrayList();

        for (int i = 0; i < 200; i++) {
            l.add(i);
        }
    }
}
