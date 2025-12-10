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

    public void removeByIdFIFO(int id) {
        if (isEmpty()) return;

        int elements = size;   // número real de elementos a procesar

        Pedido[] temp = new Pedido[elements];
        int count = 0;

        // 1. Dequeue de todos los elementos
        for (int i = 0; i < elements; i++) {
            Pedido p = dequeue();
            if (p.getId() != id) { 
                temp[count++] = p; 
            }
        }

        // 2. Enqueue de los que quedan
        for (int i = 0; i < count; i++) {
            enqueue(temp[i]);
        }
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
