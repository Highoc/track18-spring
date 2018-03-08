package ru.track.list;

import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;

public class MyTest {

    @Test(expected = NoSuchElementException.class)
    public void testStack() {
        MyLinkedList list = new MyLinkedList();
        for (int i = 0; i < 10; i++) {
            list.push(i);
        }
        Assert.assertTrue(list.size() == 10);
        for (int i = 9; i >= 0; i--){
            Assert.assertTrue(list.pop() == i);
        }
        Assert.assertTrue(list.size() == 0);
        list.pop();
    }

    @Test(expected = NoSuchElementException.class)
    public void testQueue() {
        MyLinkedList list = new MyLinkedList();
        for (int i = 0; i < 10; i++) {
            list.enqueue(i);
        }
        Assert.assertTrue(list.size() == 10);
        for (int i = 0; i < 10; i++){
            Assert.assertTrue(list.dequeu() == i);
        }
        Assert.assertTrue(list.size() == 0);
        list.dequeu();
    }
}