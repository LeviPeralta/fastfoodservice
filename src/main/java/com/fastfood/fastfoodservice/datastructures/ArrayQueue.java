package com.fastfood.fastfoodservice.datastructures;

import com.fastfood.fastfoodservice.model.Pedido;

public class ArrayQueue{
    private Pedido[] data;
    private int front;
    private int rear;
    private int size;

    public ArrayQueue() {
        this(10);
    }

    public ArrayQueue(int size) {
        this.data = new Pedido[size];
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }

    public void enqueue(Pedido element) {
        this.data[rear] = element;
        this.rear = (rear + 1) % data.length;
        size++;
    }

    public Pedido dequeue() {
        if(isEmpty()) {
            System.out.println("La cola está vacía");
            return null;
        }
        Pedido result = (Pedido) data[front];
        data[front] = null;
        front = (front + 1) % data.length;
        size--;
        return result;
    }

    public void removeById(int id) {
        int current = front;

        Pedido[] newData = new Pedido[data.length];
        int newRear = 0;
        int newSize = 0;

        for(int i = 0; i < size; i++){
            Pedido p = data[current];
            if(p.getId() != id){
                newData[newRear] = p;
                newRear = (newRear + 1) % newData.length;
                newSize++;
            }
            current = (current + 1) % data.length;
        }

        this.data = newData;
        this.front = 0;
        this.rear = newRear;
        this.size = newSize;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Pedido peek() {
        if(isEmpty()) {
            System.out.println("La cola está vacía");
            return null;
        }
        return (Pedido) data[front];
    }

    public int getSize() {
        return size;
    } 

}
