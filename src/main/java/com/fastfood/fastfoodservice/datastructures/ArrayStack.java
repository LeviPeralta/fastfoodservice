package com.fastfood.fastfoodservice.datastructures;

import java.util.Arrays;

import com.fastfood.fastfoodservice.model.HistorialOperacion;

public class ArrayStack{
    private HistorialOperacion[] data;
    private int top;

    public ArrayStack() {
        this(10);
    }

    public ArrayStack(int initialCpacity) {
        this.data = new HistorialOperacion[initialCpacity];
    }

    public void push(HistorialOperacion value) {
        addCapacity();
        this.data[top++] = value;
    }

    public HistorialOperacion pop() {
        if(isEmpty()){
            System.out.println("No hay elementos en el Stack");
            return null;
        }
        HistorialOperacion value = (HistorialOperacion) data[--top];
        data[top] = null;
        return value;
    }

    public HistorialOperacion peek() {
        if(isEmpty()){
            System.out.println("No hay elementos en el Stack");
            return null;
        }
        return (HistorialOperacion) data[top - 1];
    }

    public boolean isEmpty() {
        return top == 0;
    }

    public int size() {
        return top;
    }

    public void clear() {
        if(isEmpty()){
            System.out.println("No hay elementos en el Stack");
        }
        for (int i = top-1; i >= 0; i--) {
            data[i] = null;
        }
    }

    public void addCapacity(){
        if(top == data.length) { //Si el arreglo está lleno, vamos a aumentar la capacidad
            int newCap = data.length*2;
            //Vamos a crear un nuevo arreglo y pasarle los datos que están actualmente al nuevo
            data = Arrays.copyOf(data, newCap);
        }
    }

    public void print() {
        if(isEmpty()){
            System.out.println("No hay elementos en el Stack");
        }
        for (int i = top-1; i >= 0; i--) {
            if(data[i] != null){
                System.out.println(data[i]);
            }
        }
    }
}