package ru.track.list;
import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 *
 * Должен иметь 2 конструктора
 * - без аргументов - создает внутренний массив дефолтного размера на ваш выбор
 * - с аргументом - начальный размер массива
 */
public class MyArrayList extends List{

    private int[] data;

    private static final int baseCapacity = 8;
    private static final int addCapacity = 8;

    public MyArrayList() {
        this(baseCapacity);
    }

    public MyArrayList(int capacity) {
        data = new int[capacity];
        countNode = 0;
    }

    @Override
    public void add(int item) {
        if (countNode == data.length){
            int[] tempData = new int[data.length + addCapacity];
            System.arraycopy(data, 0, tempData, 0, data.length);
            data = tempData;
        }

        data[countNode++] = item;
    }

    @Override
    public int remove(int idx) throws NoSuchElementException {
        if ((idx < 0) || (idx >= countNode)){
            throw new NoSuchElementException();
        }

        int nowElemValue = data[idx];

        int[] tempData = new int[data.length];
        System.arraycopy(data, 0, tempData, 0, idx);
        System.arraycopy(data, idx + 1, tempData, idx, countNode - idx - 1);

        data = tempData;
        countNode--;

        return nowElemValue;
    }

    @Override
    public int get(int idx) throws NoSuchElementException {
        if ((idx < 0) || (idx >= countNode)){
            throw new NoSuchElementException();
        }

        return data[idx];
    }
}
