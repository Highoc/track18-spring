package ru.track.list;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 * Односвязный список
 */
public class MyLinkedList extends List implements Stack, Queue  {

    private Node dataHead;
    private Node dataTail;

    /**
     * private - используется для сокрытия этого класса от других.
     * Класс доступен только изнутри того, где он объявлен
     * <p>
     * static - позволяет использовать Node без создания экземпляра внешнего класса
     */
    private static class Node {
        Node prev;
        Node next;
        int val;

        Node(Node prev, Node next, int val) {
            this.prev = prev;
            this.next = next;
            this.val = val;
        }
    }

    @Override
    public void add(int item) {
        if (dataHead == null) {
            dataTail = dataHead = new Node(null, null, item);
        }
        else {
            dataTail.next = new Node(dataTail, null, item);
            dataTail = dataTail.next;
        }
        countNode++;
    }

    @Override
    public int remove(int idx) throws NoSuchElementException {
        if ((idx < 0) || (idx >= countNode)){
            throw new NoSuchElementException();
        }

        Node tempNode = null;
        if (idx == countNode - 1){
            tempNode = dataTail;
            idx = 0;
        } else {
            tempNode = dataHead;
        }

        while(idx != 0)
        {
            tempNode = tempNode.next;
            idx--;
        }

        if (tempNode.prev != null) {
            tempNode.prev.next = tempNode.next;
        } else {
            dataHead = tempNode.next;
        }

        if (tempNode.next != null) {
            tempNode.next.prev = tempNode.prev;
        } else {
            dataTail = tempNode.prev;
        }
        countNode--;

        return tempNode.val;
    }

    @Override
    public int get(int idx) throws NoSuchElementException {
        if ((idx < 0) || (idx >= countNode)){
            throw new NoSuchElementException();
        }

        Node tempNode = dataHead;
        while(idx != 0)
        {
            tempNode = tempNode.next;
            idx--;
        }

        return tempNode.val;
    }


    @Override
    public void push(int value){
        add(value);
    }

    @Override
    public int pop(){
        return remove(countNode - 1);
    }

    @Override
    public void enqueue(int value) {
        add(value);
    }

    @Override
    public int dequeu() {
        return remove(0);
    }
}
