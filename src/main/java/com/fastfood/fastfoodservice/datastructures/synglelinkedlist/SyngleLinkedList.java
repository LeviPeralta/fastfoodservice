package com.fastfood.fastfoodservice.datastructures.synglelinkedlist;

import com.fastfood.fastfoodservice.model.Pedido;

public class SyngleLinkedList {
    private ListNode head;

    public void add(Pedido data) {
        ListNode newNode = new ListNode(data);
        if(head == null) {
            head = newNode;
            return;
        }
        ListNode currentNode = head;
        while(currentNode.next != null) {
            currentNode = currentNode.next;
        }
        currentNode.next = newNode;
    }

    public Pedido finById(int id) {
        ListNode currentNode = head;

        while (currentNode != null) {
            if(currentNode.data.getId() == id) {
                return currentNode.data;
            }
            
            currentNode = currentNode.next;
        }

        System.out.println("Pedido no encontrado.");
        return null;
    }

    public void removeById(int id) {
        if(head == null) {
            return;
        }

        if(head.data.getId() == id) {
            head = head.next;
        }

        ListNode currentNode = head;
        while(currentNode.next != null && currentNode.next.data.getId() != id) {
            currentNode = currentNode.next;
        }
        if (currentNode.next != null) {
            currentNode.next.data.setEstado("CANCELADO");
            currentNode.next = currentNode.next.next;
        }
    }

    public int size() {
        int size = 0;
        ListNode currentNode = head;

        while (currentNode != null) {
            size++;
            currentNode = currentNode.next;
        }
        
        return size;
    }

    public ListNode getHead() {
        return head;
    }

    public double montoTotalRecursivo() {
        return montoTotalRecursivo(head);
    }

    private double montoTotalRecursivo(ListNode actual) {
        if (actual == null) {
            return 0; 
        }
        return actual.data.getMonto() + montoTotalRecursivo(actual.next);
    }
}
